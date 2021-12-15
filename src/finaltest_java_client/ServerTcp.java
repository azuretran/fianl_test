/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finaltest_java_client;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class ServerTcp {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, Throwable {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Server Đang Chờ kết nối từ client...");
        String kq="";
        String pass = "", username;
        boolean flag = true;
        Connection connect = DBConection.getConnection();
        BufferedWriter bufferedReader =new BufferedWriter(new FileWriter("./kq.txt"));

        int result = 0;
        while(flag) {
            Socket client = server.accept();
            System.out.println("Đã Thiết lập Kết Nối Thành Công !!!");
            // Nhan data tu sv
            DataInputStream din = new DataInputStream(client.getInputStream());
            // Gui data den sv
            DataOutputStream dout = new DataOutputStream(client.getOutputStream());
             int luaChon = din.readInt();
            switch (luaChon) {
                case 1: {
                    username = din.readUTF();
                    pass = din.readUTF();

                    String query = "	  SELECT *from dbo.login  where username=? and password=?";

                    PreparedStatement ps = connect.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, pass);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        dout.writeInt(-1);
                           
                
                    } else {
                        dout.writeInt(-2);
                    }

                }
               break;
                case 2:
                     
                       //tạo mảng sinh viên
                    ArrayList<sinhvien> sinhvienlist=new ArrayList();
                    //tạo mảng điểm sinh viên
                      ArrayList<Diem> Diemlist=new ArrayList();
                     //Lấy thông tin của sinh viên 
                        
                    String query_list_sinhvien = "	  SELECT *from LTM_SINHVIEN";

                        Statement statement=connect.createStatement();
                 
                    ResultSet rs_list_sinhvien  =  statement.executeQuery(query_list_sinhvien);
                    sinhvien user;
                    while(rs_list_sinhvien.next()){
                        user=new sinhvien(rs_list_sinhvien.getString("msv"), rs_list_sinhvien.getString("ho_lot"), rs_list_sinhvien.getString("ten"), rs_list_sinhvien.getString("malop"));
                        sinhvienlist.add(user);
                    
                    }
                   
                    
                        //Lấy thông tin điểm của sinh viên
                    
                      String query_list_diem_sinhvien = "	  SELECT *from LTM_DIEM";

                        Statement statement1=connect.createStatement();
                 
                    ResultSet rs_list_diem_sinhvien  =  statement1.executeQuery(query_list_diem_sinhvien);
                        // Tạo gói tin gởi đăng nhập thành công
                  
                    Diem user1;
                    while(rs_list_diem_sinhvien.next()){
                        user1=new Diem(rs_list_diem_sinhvien.getString("msv"),rs_list_diem_sinhvien.getFloat("diemtoan"),rs_list_diem_sinhvien.getFloat("diemvan"));
                        Diemlist.add(user1);
                    
                    }
                    System.out.println("Mã SV"+"\t"+"Họ Lót"+"\t"+"Tên"+"\t"+"Mã Lớp"+"\t"+"\t"+"Điểm TB"+"\t"+"Kết Quả");
                    kq+=("Mã SV"+"\t"+"Họ Lót"+"\t"+"Tên"+"\t"+"Mã Lớp"+"\t"+"Điểm TB"+"\t"+"Kết Quả")+"\n";
                    for (sinhvien sinhvienlist1 : sinhvienlist) {
                         for (Diem Diemlist1 : Diemlist) {
                             if(Diemlist1.getMasv().equals(sinhvienlist1.getMasv())){
                                 if((Diemlist1.getDiemtoan()+Diemlist1.getDiemvan()/2)>=5)
                                 {
                                       
                                       
                                        kq+=(sinhvienlist1.getMasv()+"\t"+sinhvienlist1.getHolot()+"\t"+
                                    sinhvienlist1.getTen()+"\t"+sinhvienlist1.getMalop()+"\t"+(Diemlist1.getDiemtoan()+Diemlist1.getDiemvan()/2)+"\t"+"D")+"\n";
                                          dout.writeUTF("./kq.txt");
                                         
                            
                     
                                        System.out.println(kq);
                                      
                                      
                                 }
                         
                                 else{
                           
                                  kq+=(sinhvienlist1.getMasv()+"\t"+sinhvienlist1.getHolot()+"\t"+
                                    sinhvienlist1.getTen()+"\t"+sinhvienlist1.getMalop()+"\t"+(Diemlist1.getDiemtoan()+Diemlist1.getDiemvan()/2)+"\t"+"R")+"\n";
                                  dout.writeUTF("./kq.txt");
                                     System.out.println(kq);
                             
             
                                     
                                 }
                             }
                         }
                      
                    }
                    flag=false;
                    break;
                  
                        
                 
                    }
               
              
            }
           bufferedReader.write(kq); 
                          bufferedReader.close();
        }

    }

