package net.iptp.server.scripts;


import net.iptp.server.DAO.Tools;
import net.iptp.server.Model.Core2.MD;
import net.iptp.server.Services.Operations.BarReader;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Ilya
 * Date: 20.01.2017
 * Time: 11:25
 *Test of Bar Reader
 */
public class BarCode {
    public static Logger log= Logger.getLogger("RequestDispatcher");

        public static void main(String[] args) throws SQLException {
            Tools tools=new Tools(null);
            tools.Connect();
        MD md=new MD(tools);
        tools.setMetadata(md);

        BarReader bar = new BarReader();
        bar.DoBarRead("/home/ilysov/Downloads/bar.pdf","/home/ilysov/Downloads/bar.pdf", tools);

        tools.Disconnect();

        String [] barcodes = bar.getBarCodes();

            for (int i = 0; i < barcodes.length; i++) {
                System.out.println("    [" + barcodes[i] + "]");
            }

        }


}