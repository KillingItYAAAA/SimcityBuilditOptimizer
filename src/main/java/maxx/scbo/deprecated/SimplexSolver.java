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

import org.apache.commons.math3.util.Precision;

/**
 * Solves a {@link LinearModel} using the Two-Phase Simplex Method.
 * 
 * @author <a href="http://www.benmccann.com">Ben McCann</a>
 */
public class SimplexSolver {

  private static final double DEFAULT_EPSILON = 0.0000000001;
  protected final SimplexTable table;
  protected final double epsilon;

  /**
   * @param model
   *          the {@link LinearModel} to solve.
   */
  public SimplexSolver(LinearModel model) {
    this(model, false);
  }

  /**
   * TODO.
   * 
   * @param model
   *          the {@link LinearModel} to solve
   * @param restrictToNonNegative
   *          whether to restrict the variables to non-negative values
   */
  public SimplexSolver(LinearModel model, boolean restrictToNonNegative) {
    this(model, restrictToNonNegative, DEFAULT_EPSILON);
  }

  /**
   * TODO.
   * 
   * @param model
   *          the {@link LinearModel} to solve
   * @param restrictToNonNegative
   *          whether to restrict the variables to non-negative values
   * @param epsilon
   *          the amount of error to accept in floating point comparisons
   */
  public SimplexSolver(LinearModel model, boolean restrictToNonNegative, double epsilon) {
    this.table = new SimplexTable(model, restrictToNonNegative);
    this.epsilon = epsilon;
  }

  /**
   * Returns the column with the most negative coefficient in the objective
   * function row.
   */
  private Integer getPivotColumn() {
    double minValue = 0;
    Integer minPos = null;
    for (int i = table.getNumObjectiveFunctions(); i < table.getWidth() - 1; i++) {
      if (table.getEntry(0, i) < minValue) {
        minValue = table.getEntry(0, i);
        minPos = i;
      }
    }
    return minPos;
  }

  /**
   * Returns the row with the minimum ratio as given by the minimum ratio test
   * (MRT).
   * 
   * @param col
   *          the column to test the ratio of. See {@link #getPivotColumn()}
   */
  private Integer getPivotRow(int col) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = table.getNumObjectiveFunctions(); i < table.getHeight(); i++) {
      double rhs = table.getEntry(i, table.getWidth() - 1);
      if (table.getEntry(i, col) >= 0) {
        double ratio = rhs / table.getEntry(i, col);
        if (ratio < minRatio) {
          minRatio = ratio;
          minRatioPos = i;
        }
      }
    }
    return minRatioPos;
  }

  /**
   * Runs one iteration of the Simplex method on the given model.
   * 
   * @throws UnboundedSolutionException
   *           if the model is found not to have a bounded solution
   */
  protected void doIteration() throws UnboundedSolutionException {
    Integer pivotCol = getPivotColumn();
    Integer pivotRow = getPivotRow(pivotCol);
    if (pivotRow == null) {
      throw new UnboundedSolutionException();
    }

    // set the pivot element to 1
    double pivotVal = table.getEntry(pivotRow, pivotCol);
    table.divideRow(pivotRow, pivotVal);

    // set the rest of the pivot column to 0
    for (int i = 0; i < table.getHeight(); i++) {
      if (i != pivotRow) {
        double multiplier = table.getEntry(i, pivotCol);
        table.subtractRow(i, pivotRow, multiplier);
      }
    }
  }

  /**
   * Checks whether Phase 1 is solved.
   * 
   * @return whether Phase 1 is solved
   */
  private boolean isPhase1Solved() {
    if (table.getNumArtificialVariables() == 0) {
      return true;
    }
    for (int i = table.getNumObjectiveFunctions(); i < table.getWidth() - 1; i++) {
      if (table.getEntry(0, i) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns whether the problem is at an optimal state.
   * 
   * @return whether the model has been solved
   */
  public boolean isOptimal() {
    if (table.getNumArtificialVariables() > 0) {
      return false;
    }
    for (int i = table.getNumObjectiveFunctions(); i < table.getWidth() - 1; i++) {
      if (table.getEntry(0, i) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Solves Phase 1 of the Simplex method.
   * 
   * @throws UnboundedSolutionException
   *           if the model is found not to have a bounded solution
   * @throws NoFeasibleSolutionException
   *           if there is no feasible solution
   */
  protected void solvePhase1() throws UnboundedSolutionException, NoFeasibleSolutionException {
    // make sure we're in Phase 1
    if (table.getNumArtificialVariables() == 0) {
      return;
    }

    while (!isPhase1Solved()) {
      doIteration();
    }

    // if W is not zero then we have no feasible solution
    if (Precision.compareTo(table.getEntry(0, table.getRhsOffset()), (double) 0, epsilon) != 0) {
      throw new NoFeasibleSolutionException();
    }
  }

  /**
   * Iterates until the optimal solution is arrived at.
   * 
   * @throws UnboundedSolutionException
   *           if the model is found not to have a bounded solution
   * @throws NoFeasibleSolutionException
   *           if there is no feasible solution
   */
  public LinearEquation solve() throws UnboundedSolutionException, NoFeasibleSolutionException {
    solvePhase1();
    table.discardArtificialVariables();
    while (!isOptimal()) {
      doIteration();
    }
    return table.getSolution();
  }

}