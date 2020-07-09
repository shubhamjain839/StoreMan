/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storeman;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author shubh
 */
public class GetTable {
    private Vector<String> coloumn;
    private Vector<Vector<String>> data;

    public GetTable() {
        coloumn = new Vector<>();
        data = new Vector<>();
    }

    public Vector<String> getColoumn() {
        return coloumn;
    }

    public Vector<Vector<String>> getData() {
        return data;
    }
    
    public TableModel setRowCol(ResultSet rs){
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            for(int i = 1; i<= colCount ;i++){
                coloumn.add(rsmd.getColumnName(i));
            }
            while(rs.next()){
                Vector<String> row = new Vector<>();
                for(int i=1;i<=colCount;i++){
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            return new DefaultTableModel(data,coloumn){
                 @Override
                 public boolean isCellEditable(int row, int column) {
                        return false;
                }   
            };
        } catch (SQLException ex) {
            Logger.getLogger(GetTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
