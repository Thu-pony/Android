package com.example.anyiassistor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderDao {
//    private static final String TAG = "mysql-party-OrederDao";
//    private List<OrderBean> orderPendingList = new ArrayList<>();
//    private List<Integer> mOrdeIndexList = new ArrayList<>();
//    private List<OrderBean> mAllOrder = new ArrayList<>();
//
//    private List<OrderBean> mProcessOrderList = new ArrayList<>();
//    private  String UID;
//    public HashMap<String, List<OrderBean>> search_order(String userAccount, Context context){
//       // User user = CurrentUser.getInstance();
//        HashMap<String, List<OrderBean>> map = new HashMap<>();
//        // 根据数据库名称，建立连接
//        Connection connection = JDBCUtils.getConn();
//        int msg = 0;
//        try {
//            // mysql简单的查询语句。这里是根据user表的userAccount字段来查询某条记录
//            String sql1 = "select UID from users where username =  ?";
//            Log.d(TAG,"sql语句" + sql1);
//            if (connection != null) {// connection不为null表示与数据库建立了连接
//                 PreparedStatement pstmt = connection.prepareStatement(sql1);
//                 //Statement stmt = connection.createStatement();
//                if (pstmt != null) {
//                    Log.e(TAG, "账号：" + userAccount);
//                    //根据账号进行查询
//                    //stmt.setString(1, userAccount);
//                    // 执行sql查询语句并返回结果集
//                    pstmt.setString(1,userAccount);
//                    ResultSet rs = pstmt.executeQuery();
//                    //ResultSet rs = stmt.executeQuery(sql1);
//                    //int count = rs.getMetaData().getColumnCount();
//                    //将查到的内容储存在map里
//                    if (rs.next()) {
//                        UID = rs.getString("UID");
//                        Log.e(TAG, "ID：" + UID);
//                        // 注意：下标是从1开始的
//                        /* for (int i = 1; i <= count; i++) {
//                            String field = rs.getMetaData().getColumnName(i);
//                            map.put(field, rs.getString(field));
//                            if (field.equals("UID"))
//                                UID = rs.getString(field);
//                        } */
//                    }
//                    pstmt.close();
//                    String sql2 = "select order_order_id from user_order_relations where user_UID = ?";
//                    PreparedStatement pstmt2 = connection.prepareStatement(sql2);
//                    if (pstmt2 != null) {
//                        Log.e(TAG, "ID：" + UID);
//                        pstmt2.setString(1,UID);
//                        rs = pstmt2.executeQuery();
//                        //根据账号进行查询
//                        //stmt.setString(1, UID);
//                        // 执行sql查询语句并返回结果集
//                        //rs = stmt.executeQuery(sql2);
//                        int count = rs.getMetaData().getColumnCount();
//                        //将查到的内容储存在map里
//                        while (rs.next()) {
//                            // 注意：下标是从1开始的
//                            for (int i = 1; i <= count; i++) {
//                                String field = rs.getMetaData().getColumnName(i);
//                                Log.d(TAG,field);
//                                //map.put(field, rs.getString(field));
//                                if (field.equals("order_order_id"))
//                                   mOrdeIndexList.add(rs.getInt(field));
//                            }
//
//
//                        }
//                        Log.d(TAG,mOrdeIndexList.size() + "待处理的数据");
//                        pstmt2.close();
//                        String sql3 = "select * from orders where order_id = ?";
//                        PreparedStatement pstmt3 = connection.prepareStatement(sql3);
//                        if (pstmt3 != null) {
//                            for (int i = 0; i < mOrdeIndexList.size(); ++i) {
//                                Log.d(TAG,"待处理的订单号 " + mOrdeIndexList.get(i));
//                                pstmt3.setInt(1, mOrdeIndexList.get(i));
//                                rs = pstmt3.executeQuery();
//                                count = rs.getMetaData().getColumnCount();
//                                while (rs.next()) {
//                                    String title = null;
//                                    if (rs.getString(4).equals("待处理"))
//                                        title = "待处理 订单号： " + "B000" + rs.getInt(1) + "产品类型 " + rs.getString(2);
//                                    else if (rs.getString(4).equals("处理中"))
//                                        title = "进行中 订单号： " + "A000" + rs.getInt(1) + "产品类型 " + rs.getString(2);
//                                    int src = R.drawable.item1;
//                                    String info = "创建时间： " + rs.getString(3) ;
//                                    OrderBean tempOrder = new OrderBean(rs.getInt(1),title,src,info);
//                                    //orderPendingList.add(tempOrder);
//                                    mAllOrder.add(tempOrder);
//                                    if (rs.getString(4).equals("待处理"))
//                                        orderPendingList.add(tempOrder);
//                                    else if (rs.getString(4).equals("处理中"))
//                                        mProcessOrderList.add(tempOrder);
//                                }
//                            }
//                        }
//                        pstmt3.close();
//                        connection.close();
//                    }
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        Log.d(TAG,"全部订单数: " + mAllOrder.size());
//        Log.d(TAG,"待处理: " + orderPendingList.size());
//        Log.d(TAG,"进行中: " + mProcessOrderList.size());
//        map.put("全部订单",mAllOrder);
//        map.put("待处理",orderPendingList);
//        map.put("进行中",mProcessOrderList);
//        return map;
//    }
//    public boolean Modify_State(OrderBean orderBean, String target, Context context) {
//        int Orderid = orderBean.getOrder_id();
//        Connection connection = JDBCUtils.getConn();
//       // String sql1 = "select UID from users where username =  ?";
//        //Log.d(TAG,"sql语句" + sql1);
//        if (connection != null) {
//           String sql = "select status from orders where order_id = ?";
//           String sql1 = "UPDATE orders SET status = ? WHERE order_id = ?";
//            try {
//                PreparedStatement preparedStatement = connection.prepareStatement(sql);
//                if (preparedStatement != null) {
//                    preparedStatement.setInt(1,Orderid);
//                    ResultSet rs = preparedStatement.executeQuery();
//                    while (rs.next()) {
//                        String now = rs.getString(1);
//                        if (now.equals("待处理") && target.equals("已完成")) {
//                            //Toast.makeText(context, "不可以将待处理的直接设为已完成", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }
//                        if (now.equals(target)) {
//                            //Toast.makeText(context, "现在已经是 " + now + "状态", Toast.LENGTH_SHORT).show();
//                            return false;
//                        }
//                    }
//                    preparedStatement.close();
//                    PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
//                    if (preparedStatement1 != null) {
//                        preparedStatement1.setString(1, target);
//                        preparedStatement1.setInt(2,Orderid);
//                        int rows = preparedStatement1.executeUpdate();
//                        if (rows == 0) {
//                            return false;
//                        }
//                    }
//                    preparedStatement1.close();
//                    //int rows = preparedStatement.executeUpdate();
//
//                }
//
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//        try {
//            connection.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return true;
//    }
}
