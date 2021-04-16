package xyz.yuanjin.project.common.util;

/**
 * @author yuanjin
 */
public class RowReader {
    private Integer curRow = -1;

    public void printRow(int sheetIndex, int curRow, String[] row) {
        if (this.curRow != curRow) {
            System.out.print("sheet:" + sheetIndex + "-row:" + curRow + " ");
            for (String cell : row) {
                System.out.print(cell + "  ");
            }
            System.out.println();
            this.curRow = curRow;
        }

    }
}
