import { colorMapping, makeTime } from 'thirdeye-frontend/utils/rca-utils';
import {
  get,
  computed,
  getProperties
} from '@ember/object';
import Controller from '@ember/controller';
import { humanizeFloat } from 'thirdeye-frontend/utils/utils';
import moment from 'moment';
import _ from 'lodash';

const TABLE_DATE_FORMAT = 'MMM DD, hh:mm A'; // format for anomaly table and legend

export default Controller.extend({
  /**
   * Anomaly data, fetched using the anomalyId
   */
  anomalyData: {},
  /**
   * current time series
   */
  current: null,
  /**
   * predicted time series
   */
  predicted: null,
  /**
   * imported color mapping for graph
   */
  colorMapping: colorMapping,

  zoom: {
    enabled: false,
    rescale: true
  },

  // legend and point are for the graph
  legend: {
    show: true,
    position: 'right'
  },

  point: {
    show: false
  },

  anomaly: computed(
    'anomalyData',
    function() {
      return !_.isEmpty(get(this, 'anomalyData'));
    }
  ),

  series: computed(
    'anomalyData',
    'current',
    'predicted',
    function () {
      const {
        anomalyData, current, predicted
      } = getProperties(this, 'anomalyData', 'current', 'predicted');

      const series = {};

      if (!_.isEmpty(anomalyData)) {
        const key = this._formatAnomaly(anomalyData);
        series[key] = {
          timestamps: [anomalyData.startTime, anomalyData.endTime],
          values: [1, 1],
          type: 'region',
          color: 'orange'
        };
      }

      if (current && !_.isEmpty(current.value)) {
        series['current'] = {
          timestamps: current.timestamp,
          values: current.value,
          type: 'line',
          color: 'blue'
        };
      }

      if (predicted && !_.isEmpty(predicted.value)) {
        series['predicted'] = {
          timestamps: predicted.timestamp,
          values: predicted.value,
          type: 'line',
          color: 'orange'
        };
      }
      return series;
    }
  ),

  axis: computed(
    'anomalyData',
    function () {
      const anomalyData = get(this, 'anomalyData');

      return {
        y: {
          show: true,
          tick: {
            format: function(d){return humanizeFloat(d);}
          }
        },
        y2: {
          show: false,
          min: 0,
          max: 1
        },
        x: {
          type: 'timeseries',
          show: true,
          min: anomalyData.startTime,
          max: anomalyData.endTime,
          tick: {
            fit: false,
            format: (d) => {
              const t = makeTime(d);
              if (t.valueOf() === t.clone().startOf('day').valueOf()) {
                return t.format('MMM D (ddd)');
              }
              return t.format('h:mm a');
            }
          }
        }
      };
    }
  ),

  _formatAnomaly(anomaly) {
    return `${moment(anomaly.startTime).format(TABLE_DATE_FORMAT)}`;
  }
});
