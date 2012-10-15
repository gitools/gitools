/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.matrix.model;


import org.gitools.matrix.DiagonalMatrix;
import org.gitools.matrix.DiagonalMatrixView;


public class MatrixFactory {

    /*
    Creates an IMatrix of the same class as the MatrixView
    contents but only containing the visibleRows() and
    visibleColumns(). A diagonal matrix will be converted
    in a non-diagonal matrix
     */
    public static IMatrix create(IMatrixView matrixView) {

        IMatrix contents = matrixView.getContents();
        Class contentsClass = contents.getClass();

        boolean diagonal = false;
        if (matrixView instanceof DiagonalMatrixView) {
            diagonal = true;
        }


        int rowNb = matrixView.getRowCount();
        int colNb = matrixView.getColumnCount();
        int dimNb = matrixView.getCellAdapter().getPropertyCount();

        String[] rowNames = new String[rowNb];
        String[] colNames = new String[colNb];

        for (int i = 0; i < rowNb; i++)
            rowNames[i] = matrixView.getRowLabel(i);
        for (int j = 0; j < colNb; j++)
            colNames[j] = matrixView.getColumnLabel(j);

      
        IMatrix matrix = null;

        if (contentsClass == ObjectMatrix.class) {

            ObjectMatrix ob = ((ObjectMatrix) contents);
            matrix = new ObjectMatrix(ob.getTitle(),rowNames,colNames,ob.getCellAdapter());
      
        } else if (contentsClass == DoubleBinaryMatrix.class) {

            DoubleBinaryMatrix dbm = ((DoubleBinaryMatrix) contents);
            matrix =  new DoubleBinaryMatrix(dbm.getTitle(),colNames,rowNames);
            
        } else if (contentsClass == DoubleMatrix.class) {
            
            DoubleMatrix dm = ((DoubleMatrix) contents);
            matrix = new DoubleMatrix(dm.getTitle(),colNames,rowNames);
            
        } else if (contentsClass == DiagonalMatrix.class) {
            DiagonalMatrix dm = ((DiagonalMatrix) contents);
            matrix = new ObjectMatrix("",rowNames,colNames, dm.getCellAdapter());
        }/* else if (c == StringMatrix.class) {

            
        } else if (c == AnnotationMatrix.class) {
            
        }   */



        for (int r = 0; r < rowNb; r++) {
            for (int c = 0; c < colNb; c++) {
                for (int d = 0; d < dimNb; d++) {
                    Object v;
                    if (diagonal && c < r)
                        v = matrixView.getCellValue(c,r,d);
                    else
                        v = matrixView.getCellValue(r,c,d);
                    matrix.setCellValue(r,c,d,v);
                }
            }
        }

        return matrix;
    }

}
