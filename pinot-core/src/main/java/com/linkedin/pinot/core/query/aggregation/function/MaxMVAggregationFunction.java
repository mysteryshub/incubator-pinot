/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.query.aggregation.function;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.core.common.BlockValSet;
import com.linkedin.pinot.core.query.aggregation.AggregationResultHolder;
import com.linkedin.pinot.core.query.aggregation.groupby.GroupByResultHolder;
import javax.annotation.Nonnull;


public class MaxMVAggregationFunction extends MaxAggregationFunction {
  private static final String NAME = AggregationFunctionFactory.AggregationFunctionType.MAXMV.getName();

  @Nonnull
  @Override
  public String getName() {
    return NAME;
  }

  @Nonnull
  @Override
  public String getColumnName(@Nonnull String[] columns) {
    return NAME + "_" + columns[0];
  }

  @Override
  public void aggregate(int length, @Nonnull AggregationResultHolder aggregationResultHolder,
      @Nonnull BlockValSet... blockValSets) {
    FieldSpec.DataType dataType = blockValSets[0].getValueType();
    switch (dataType) {
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
      case DOUBLE:
        double[][] doubleValuesArray = blockValSets[0].getDoubleValuesMV();
        double maxDouble = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < length; i++) {
          for (double value : doubleValuesArray[i]) {
            maxDouble = Math.max(maxDouble, value);
          }
        }
        setAggregationResult(aggregationResultHolder, maxDouble);
        break;

      case STRING:
        String[][] stringValuesArray = blockValSets[0].getStringValuesMV();
        String maxString = null;
        for (int i = 0; i < length; i++) {
          for (String value : stringValuesArray[i]) {
            if (maxString == null || value.compareTo(maxString) > 0) {
              maxString = value;
            }
          }
        }
        setAggregationResult(aggregationResultHolder, maxString);
        break;

      default:
        throw new IllegalArgumentException("Maxmv operation not supported on datatype " + dataType);
    }
  }

  @Override
  public void aggregateGroupBySV(int length, @Nonnull int[] groupKeyArray,
      @Nonnull GroupByResultHolder groupByResultHolder, @Nonnull BlockValSet... blockValSets) {
    FieldSpec.DataType dataType = blockValSets[0].getValueType();
    switch (dataType) {
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
      case DOUBLE:
        double[][] doubleValuesArray = blockValSets[0].getDoubleValuesMV();
        for (int i = 0; i < length; i++) {
          double maxDouble = findMaxDouble(doubleValuesArray[i].length, doubleValuesArray[i]);
          setGroupByResult(groupKeyArray[i], groupByResultHolder, maxDouble);
        }
        break;

      case STRING:
        String[][] stringValuesArray = blockValSets[0].getStringValuesMV();
        for (int i = 0; i < length; i++) {
          String maxString = findMaxString(stringValuesArray[i].length, stringValuesArray[i]);
          setGroupByResult(groupKeyArray[i], groupByResultHolder, maxString);
        }
        break;

      default:
        throw new IllegalArgumentException("Maxmv operation not supported on datatype " + dataType);
    }
  }

  @Override
  public void aggregateGroupByMV(int length, @Nonnull int[][] groupKeysArray,
      @Nonnull GroupByResultHolder groupByResultHolder, @Nonnull BlockValSet... blockValSets) {
    FieldSpec.DataType dataType = blockValSets[0].getValueType();
    switch (dataType) {
      case FLOAT:
      case INT:
      case LONG:
      case SHORT:
      case DOUBLE:
        double[][] doubleValuesArray = blockValSets[0].getDoubleValuesMV();
        for (int i = 0; i < length; i++) {
          double maxDouble = findMaxDouble(doubleValuesArray[i].length, doubleValuesArray[i]);
          for (int groupKey : groupKeysArray[i]) {
            setGroupByResult(groupKey, groupByResultHolder, maxDouble);
          }
        }
        break;

      case STRING:
        String[][] stringValuesArray = blockValSets[0].getStringValuesMV();
        for (int i = 0; i < length; i++) {
          String maxString = findMaxString(stringValuesArray[i].length, stringValuesArray[i]);
          for (int groupKey : groupKeysArray[i]) {
            setGroupByResult(groupKey, groupByResultHolder, maxString);
          }
        }
        break;

      default:
        throw new IllegalArgumentException("Maxmv operation not supported on datatype " + dataType);
    }
  }

}
