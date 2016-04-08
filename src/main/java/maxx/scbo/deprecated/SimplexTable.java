// Copyright 2009 Google Inc.

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package maxx.scbo.deprecated;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A table for use in the Simplex method.
 * 
 * <p>
 * Example: W | Z | x1 | x2 | x- | s1 | s2 | a1 | RHS
 * --------------------------------------------------- -1 0 0 0 0 0 0 1 0 <=
 * phase 1 objective 0 1 -15 -10 0 0 0 0 0 <= phase 2 objective 0 0 1 0 0 1 0 0
 * 2 <= constraint 1 0 0 0 1 0 0 1 0 3 <= constraint 2 0 0 1 1 0 0 0 1 4 <=
 * constraint 3
 * </p>
 * 
 * <p>
 * W: Phase 1 objective function Z: Phase 2 objective function x1 & x2: Decision
 * variables x-: Extra decision variable to allow for negative values s1 & s2:
 * Slack/Surplus variables a1: Artificial variable RHS: Right hand side
 * </p>
 * 
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
class SimplexTable {

  protected RealMatrix table;
  protected final boolean nonNegative;
  protected final int numDecisionVariables;
  protected final int numSlackVariables;
  protected int numArtificialVariables;

  SimplexTable(LinearModel model) {
    this(model, false);
  }

  SimplexTable(LinearModel model, boolean restrictToNonNegative) {
    Map<Relationship, Integer> counts = model.getConstraintTypeCounts();
    this.nonNegative = restrictToNonNegative;
    this.numDecisionVariables = model.getNumVariables() + (nonNegative ? 0 : 1);
    this.numSlackVariables = counts.get(Relationship.LEQ) + counts.get(Relationship.GEQ);
    this.numArtificialVariables = counts.get(Relationship.EQ) + counts.get(Relationship.GEQ);
    this.table = new Array2DRowRealMatrix(createTable(model));
    initialize();
  }

  protected double[][] createTable(LinearModel model) {

    // create a matrix of the correct size
    int width = this.numDecisionVariables + this.numSlackVariables + this.numArtificialVariables
        + getNumObjectiveFunctions() + 1; // + 1 is for RHS
    int height = model.getConstraints().size() + getNumObjectiveFunctions();
    double[][] matrix = new double[height][width];
    for (int i = 1; i < height; i++) {
      Arrays.fill(matrix[i], 0);
    }

    // initialize the objective function rows
    ExLinearObjectiveFunction objectiveFunction = model.getObjectiveFunction();
    if (getNumObjectiveFunctions() == 2) {
      matrix[0][0] = -1;
    }
    int zindex = getNumObjectiveFunctions() == 1 ? 0 : 1;
    boolean maximize = objectiveFunction.getGoalType() == GoalType.MAXIMIZE;
    matrix[zindex][zindex] = maximize ? 1 : -1;
    ArrayRealVector objectiveCoefficients = maximize
        ? (ArrayRealVector) model.getObjectiveFunction().getCoefficients().mapMultiply(-1)
        : (ArrayRealVector) model.getObjectiveFunction().getCoefficients();
    copyArray(objectiveCoefficients.getDataRef(), matrix[zindex], getDecisionVariableOffset());
    matrix[zindex][width - 1] = maximize ? model.getObjectiveFunction().getConstantTerm()
        : -1 * model.getObjectiveFunction().getConstantTerm();

    if (!nonNegative) {
      matrix[zindex][getSlackVariableOffset() - 1] = getInvertedCoeffiecientSum(
          objectiveCoefficients);
    }

    // initialize the constraint rows
    List<LinearEquation> constraints = model.getNormalizedConstraints();
    int slackVar = 0;
    int artificialVar = 0;
    for (int i = 0; i < constraints.size(); i++) {
      LinearEquation constraint = constraints.get(i);
      int row = getNumObjectiveFunctions() + i;

      // decision variable coefficients
      copyArray(constraint.getCoefficients().getDataRef(), matrix[row], 1);

      // x-
      if (!nonNegative) {
        matrix[row][getSlackVariableOffset() - 1] = getInvertedCoeffiecientSum(
            constraint.getCoefficients());
      }

      // RHS
      matrix[row][width - 1] = constraint.getRightHandSide();

      // slack variables
      if (constraint.getRelationship() == Relationship.LEQ) {
        matrix[row][getSlackVariableOffset() + slackVar++] = 1; // slack
      } else if (constraint.getRelationship() == Relationship.GEQ) {
        matrix[row][getSlackVariableOffset() + slackVar++] = -1; // excess
      }

      // artificial variables
      if (constraint.getRelationship() == Relationship.EQ
          || constraint.getRelationship() == Relationship.GEQ) {
        matrix[0][getArtificialVariableOffset() + artificialVar] = 1;
        matrix[row][getArtificialVariableOffset() + artificialVar++] = 1;
      }
    }

    return matrix;
  }

  /**
   * Returns the number of objective functions in this table.
   * 
   * @return 2 for Phase 1. 1 for Phase 2.
   */
  protected final int getNumObjectiveFunctions() {
    return this.numArtificialVariables > 0 ? 2 : 1;
  }

  /**
   * Puts the table in proper form by zeroing out the artificial variables in
   * the objective function via elementary row operations.
   */
  private void initialize() {
    for (int artificialVar = 0; artificialVar < numArtificialVariables; artificialVar++) {
      int row = getBasicRow(getArtificialVariableOffset() + artificialVar);
      subtractRow(0, row, 1.0);
    }
  }

  /**
   * Returns the -1 times the sum of all coefficients in the given array.
   */
  protected static double getInvertedCoeffiecientSum(ArrayRealVector coefficients) {
    double sum = 0;
    for (double coefficient : coefficients.getDataRef()) {
      sum -= coefficient;
    }
    return sum;
  }

  /**
   * Checks whether the given column is basic.
   * 
   * @return the row that the variable is basic in. null if the column is not
   *         basic
   */
  private Integer getBasicRow(int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
      if (getEntry(i, col) != 0.0) {
        if (row == null) {
          row = i;
        } else {
          return null;
        }
      }
    }
    return row;
  }

  /**
   * Removes the phase 1 objective function and artificial variables from this
   * table.
   */
  protected void discardArtificialVariables() {
    if (numArtificialVariables == 0) {
      return;
    }
    int width = getWidth() - numArtificialVariables - 1;
    int height = getHeight() - 1;
    double[][] matrix = new double[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width - 1; j++) {
        matrix[i][j] = getEntry(i + 1, j + 1);
      }
      matrix[i][width - 1] = getEntry(i + 1, getRhsOffset());
    }
    this.table = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
  }

  /**
   * TODO.
   * 
   * @param src
   *          the source array
   * @param dest
   *          the destination array
   * @param destPos
   *          the destination position
   */
  private void copyArray(double[] src, double[] dest, int destPos) {
    System.arraycopy(src, 0, dest, getNumObjectiveFunctions(), src.length);
  }

  /**
   * Returns the current solution. {@link #solve} should be called first for
   * this to be the optimal solution.
   */
  protected LinearEquation getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    double mostNegative = getDecisionVariableValue(getOriginalNumDecisionVariables());
    for (int i = 0; i < coefficients.length; i++) {
      coefficients[i] = nonNegative ? getDecisionVariableValue(i)
          : getDecisionVariableValue(i) - mostNegative;
    }
    return new LinearEquation(coefficients, Relationship.EQ,
        table.getEntry(0, 0) * table.getEntry(0, getRhsOffset()));
  }

  /**
   * Returns the value of the given decision variable. This is not the actual
   * value as it is guaranteed to be >= 0 and thus must be corrected before
   * being returned to the user.
   * 
   * @param decisionVariable
   *          The index of the decision variable
   * @return The value of the given decision variable.
   */
  protected double getDecisionVariableValue(int decisionVariable) {
    Integer basicRow = getBasicRow(getNumObjectiveFunctions() + decisionVariable);
    return basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
  }

  /**
   * Subtracts a multiple of one row from another. After application of this
   * operation, the following will hold: minuendRow = minuendRow - multiple *
   * subtrahendRow
   */
  protected void divideRow(int dividendRow, double divisor) {
    for (int j = 0; j < getWidth(); j++) {
      table.setEntry(dividendRow, j, table.getEntry(dividendRow, j) / divisor);
    }
  }

  /**
   * Subtracts a multiple of one row from another. After application of this
   * operation, the following will hold: minuendRow = minuendRow - multiple *
   * subtrahendRow
   */
  protected void subtractRow(int minuendRow, int subtrahendRow, double multiple) {
    for (int j = 0; j < getWidth(); j++) {
      table.setEntry(minuendRow, j,
          table.getEntry(minuendRow, j) - multiple * table.getEntry(subtrahendRow, j));
    }
  }

  protected final int getWidth() {
    return table.getColumnDimension();
  }

  protected final int getHeight() {
    return table.getRowDimension();
  }

  protected final double getEntry(int row, int column) {
    return table.getEntry(row, column);
  }

  protected final void setEntry(int row, int column, double value) {
    table.setEntry(row, column, value);
  }

  protected final int getDecisionVariableOffset() {
    return getNumObjectiveFunctions();
  }

  protected final int getSlackVariableOffset() {
    return getNumObjectiveFunctions() + numDecisionVariables;
  }

  protected final int getArtificialVariableOffset() {
    return getNumObjectiveFunctions() + numDecisionVariables + numSlackVariables;
  }

  protected final int getRhsOffset() {
    return getWidth() - 1;
  }

  /**
   * If variables are not restricted to positive values, this will include 1
   * extra decision variable to represent the absolute value of the most
   * negative variable.
   */
  protected final int getNumDecisionVariables() {
    return numDecisionVariables;
  }

  protected final int getOriginalNumDecisionVariables() {
    return nonNegative ? numDecisionVariables : numDecisionVariables - 1;
  }

  protected final int getNumSlackVariables() {
    return numSlackVariables;
  }

  protected final int getNumArtificialVariables() {
    return numArtificialVariables;
  }

  protected final double[][] getData() {
    return table.getData();
  }

}
