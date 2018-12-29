package com.hw.survey.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.widget.Toast;

import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.hw.survey.family.Trip;
import com.hw.survey.family.TripWay;
import com.hw.survey.family.User;
import com.hw.survey.family.Users;
import com.hw.survey.family.XiaoQu;

public class ExcelUtils {
    //内存地址
    public static String root = Environment.getExternalStorageDirectory()
            .getPath();

    private static final String intDone = "1";
    private static final String intUnDone = "0";
    private static final String textYes = "是";

    private static String insertString(String input){
        if(TextUtils.isEmpty(input)){
            return intUnDone;
        }else{
            return input;
        }
    }

    private static String isSelected(boolean selected){
        if(selected){
            return intDone;
        }else {
            return intUnDone;
        }
    }

    public static void writeExcel(Context context, Users users,
                                  String fileName) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)&&getAvailableStorage()>1000000) {
            Toast.makeText(context, "SD卡不可用", Toast.LENGTH_LONG).show();
            return;
        }

        File dir = new File(root);
        File file = new File(dir, fileName + ".xls");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 创建Excel工作表
        WritableWorkbook wwb;
        OutputStream os = new FileOutputStream(file);
        wwb = Workbook.createWorkbook(os);

        String[] sheQuTitle = { "序号", "小区地址经度(BD09)", "小区地址纬度(BD09)","小区地址经度(WGS84)", "小区地址纬度(WGS84)", "小区地址名称","竣工年代","入住率",
                "社区服务站","警务室","日间照料中心",
                "多功能运动场","卫生服务站","社区文化活动室","菜店",
                "街道公共服务中心","街道办事处","派出所","卫生服务中心","老年服务中心","街道文化中心","全民健身活动中心","区名称","街道名称","社区名称"
        };

        WritableSheet sheetSheQu = wwb.createSheet("社区信息", 0);
        for (int i = 0; i < sheQuTitle.length; i++) {
            sheetSheQu.addCell(new Label(i, 0, sheQuTitle[i], getHeader()));
        }

        int sheQuLine = 0;

        String[] familyTitle = { "家庭编号", "是否完成", "区", "街道","社区","调查员姓名","联系方式",
                "家庭地址经度(BD09)","家庭地址纬度(BD09)","家庭地址经度(WGS84)","家庭地址纬度(WGS84)","家庭地址名称","总人数","不满六周岁人数","中小学生人数","小汽车数量",
                "车牌归属地","摩托车数量","夜间停放地点","月均夜间停车费","购车计划","是否考虑购买新能源汽车","套内住房面积","家庭月均总收入","房屋产权","户主或某一家庭成员手机号码"
        };

        // 添加第一个工作表并设置家庭信息Sheet的名字
        WritableSheet sheetFamily = wwb.createSheet("家庭信息", 1);
        Label label;
        for (int i = 0; i < familyTitle.length; i++) {
            // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
            // 在Label对象的子对象中指明单元格的位置和内容
            label = new Label(i, 0, familyTitle[i], getHeader());
            // 将定义好的单元格添加到工作表中
            sheetFamily.addCell(label);
        }

        int familyLine = 0;

        String[] memberTitle = { "个人编号", "是否完成", "家庭编号", "工作地地址经度(BD09)","工作地地址纬度(BD09)",
                "工作地地址经度(WGS84)","工作地地址纬度(WGS84)","工作地地址名称",
                "是否有固定工作地","性别","年龄","户籍情况","非杭州户籍居住时间",
                "职业","就学情况","上班常用出行方式","单位停车场类别","单位停车场收费","移动号数量","联通号数量",
                "电信号数量","是否更换本地号","手机注册名","乘坐飞机次数","乘坐火车次数","前往重庆市主城区以外区县的次数",
                "主要目的地","出行目的","出行方式","无出行的原因"
        };

        WritableSheet sheetMember = wwb.createSheet("个人信息", 2);
        for (int i = 0; i < memberTitle.length; i++) {
            sheetMember.addCell(new Label(i, 0, memberTitle[i], getHeader()));
        }

        int memberLine = 0;

        String[] tripTitle = { "出行编号", "出行次序", "成员编号", "出行目的","出发时间","出发地经度(BD09)","出发地纬度(BD09)","出发地经度(WGS84)","出发地纬度(WGS84)",
                "出发地名称","出发地性质","到达时间","到达地经度(BD09)","到达地纬度(BD09)","到达地经度(WGS84)","到达地纬度(WGS84)","到达地名称",
                "到达地性质","交通方式1","轨道线路号1","步行至取车地或候车地时间","候车时间1","停车场类别1","停车费用1","交通方式2","轨道线路号2",
                "换乘时间1","候车时间2","停车场类别2","停车费用2","交通方式3","轨道线路号3","换乘时间2","候车时间3","停车场类别3","停车费用3","交通方式4","轨道线路号4",
                "换乘时间3","候车时间4","停车场类别4","停车费用4","交通方式5","轨道线路号5","换乘时间4","候车时间5","停车场类别5","停车费用5","下车后步行至终点时间","出行费用","停车费用","同行人数","是否包含家人"
        };

        WritableSheet sheetTrip = wwb.createSheet("出行信息", 3);
        for (int i = 0; i < tripTitle.length; i++) {
            sheetTrip.addCell(new Label(i, 0, tripTitle[i], getHeader()));
        }

        int tripLine = 0;


        //填入数据
        List<User> userList = users.getUsers();
        if(userList != null && userList.size() > 0 ){
            for(User user : userList){
                List<XiaoQu> xiaoQuList = user.xiaoQuses;
                if(xiaoQuList != null && xiaoQuList.size() > 0){
                    for(XiaoQu xiaoQu:xiaoQuList){
                        if(xiaoQu.isDone()){
                            int xiaoQuId = ++sheQuLine;
                            sheetSheQu.addCell(new Label(0,xiaoQuId,String.valueOf(xiaoQuId)));
                            double[] address = AddressUtils.getAddressFromString(xiaoQu.address);
                            double[] address_w = MapUtils.bd09ToWgs84(address[1],address[0]);
                            sheetSheQu.addCell(new Label(1,xiaoQuId,String.valueOf(address[1])));
                            sheetSheQu.addCell(new Label(2,xiaoQuId,String.valueOf(address[0])));
                            sheetSheQu.addCell(new Label(3,xiaoQuId,String.valueOf(address_w[0])));
                            sheetSheQu.addCell(new Label(4,xiaoQuId,String.valueOf(address_w[1])));
                            sheetSheQu.addCell(new Label(5,xiaoQuId,insertString(xiaoQu.name)));
                            sheetSheQu.addCell(new Label(6,xiaoQuId,insertString(xiaoQu.age)));
                            sheetSheQu.addCell(new Label(7,xiaoQuId,String.valueOf(xiaoQu.rate)));
                            sheetSheQu.addCell(new Label(8,xiaoQuId,isSelected(xiaoQu.isSelect01)));
                            sheetSheQu.addCell(new Label(9,xiaoQuId,isSelected(xiaoQu.isSelect02)));
                            sheetSheQu.addCell(new Label(10,xiaoQuId,isSelected(xiaoQu.isSelect03)));
                            sheetSheQu.addCell(new Label(11,xiaoQuId,isSelected(xiaoQu.isSelect04)));
                            sheetSheQu.addCell(new Label(12,xiaoQuId,isSelected(xiaoQu.isSelect05)));
                            sheetSheQu.addCell(new Label(13,xiaoQuId,isSelected(xiaoQu.isSelect06)));
                            sheetSheQu.addCell(new Label(14,xiaoQuId,isSelected(xiaoQu.isSelect07)));
                            sheetSheQu.addCell(new Label(15,xiaoQuId,isSelected(xiaoQu.isSelect11)));
                            sheetSheQu.addCell(new Label(16,xiaoQuId,isSelected(xiaoQu.isSelect12)));
                            sheetSheQu.addCell(new Label(17,xiaoQuId,isSelected(xiaoQu.isSelect13)));
                            sheetSheQu.addCell(new Label(18,xiaoQuId,isSelected(xiaoQu.isSelect14)));
                            sheetSheQu.addCell(new Label(19,xiaoQuId,isSelected(xiaoQu.isSelect15)));
                            sheetSheQu.addCell(new Label(20,xiaoQuId,isSelected(xiaoQu.isSelect16)));
                            sheetSheQu.addCell(new Label(21,xiaoQuId,isSelected(xiaoQu.isSelect17)));
                            sheetSheQu.addCell(new Label(22,xiaoQuId,insertString(user.qu)));
                            sheetSheQu.addCell(new Label(23,xiaoQuId,insertString(user.street)));
                            sheetSheQu.addCell(new Label(24,xiaoQuId,insertString(user.shequ)));
                        }
                    }
                }

                if(user.families == null || user.families.size() < 1){
                    continue;
                }
                for (int i = 0; i < user.families.size(); i++) {
                    int familyId = ++familyLine;
                    Family family = user.families.get(i);
                    sheetFamily.addCell(new Label(0, familyId, String.valueOf(familyId)));
                    sheetFamily.addCell(new Label(1, familyId, family.isAllDone()?intDone:intUnDone));
                    sheetFamily.addCell(new Label(2, familyId, insertString(user.qu)));//区
                    sheetFamily.addCell(new Label(3, familyId, insertString(user.street)));//街道
                    sheetFamily.addCell(new Label(4, familyId, insertString(user.shequ)));//社区
                    sheetFamily.addCell(new Label(5, familyId, insertString(user.name)));//调查员姓名
                    sheetFamily.addCell(new Label(6, familyId, insertString(user.phone)));//联系方式
                    double[] familyAddress = AddressUtils.getAddressFromString(family.address);
                    double[] familyAddress1 = MapUtils.bd09ToWgs84(familyAddress[1],familyAddress[0]);
                    sheetFamily.addCell(new Label(7, familyId, String.valueOf(familyAddress[1])));//家庭地址经纬度
                    sheetFamily.addCell(new Label(8, familyId, String.valueOf(familyAddress[0])));
                    sheetFamily.addCell(new Label(9, familyId, String.valueOf(familyAddress1[0])));//家庭地址经纬度
                    sheetFamily.addCell(new Label(10, familyId, String.valueOf(familyAddress1[1])));
                    sheetFamily.addCell(new Label(11, familyId, family.addressDetail));
                    sheetFamily.addCell(new Label(12, familyId, String.valueOf(family.totalNum)));
                    sheetFamily.addCell(new Label(13, familyId, String.valueOf(family.up6Num)));
                    sheetFamily.addCell(new Label(14, familyId, String.valueOf(family.tempNum)));
                    sheetFamily.addCell(new Label(15, familyId, String.valueOf(family.carNum)));
                    sheetFamily.addCell(new Label(16, familyId, insertString(family.carAddress)));
                    sheetFamily.addCell(new Label(17, familyId, String.valueOf(family.motoNum)));
                    sheetFamily.addCell(new Label(18, familyId, insertString(family.stopPlace)));
                    sheetFamily.addCell(new Label(19, familyId, insertString(family.stopFee)));
                    sheetFamily.addCell(new Label(21, familyId, insertString(family.batteryCar)));
                    sheetFamily.addCell(new Label(22, familyId, insertString(family.houseSize)));
                    sheetFamily.addCell(new Label(25, familyId, insertString(family.phone)));

                    List<Person> members = family.people;
                    if(members != null && members.size() > 0){
                        for (Person member : members){
                            int memberId = ++ memberLine;
                            sheetMember.addCell(new Label(0,memberId,String.valueOf(memberId)));
                            sheetMember.addCell(new Label(1,memberId,member.isAllDone()?intDone:intUnDone));
                            sheetMember.addCell(new Label(2,memberId,String.valueOf(familyId)));
                            double[] addressMember = AddressUtils.getAddressFromString(member.address);
                            double[] addressMember1 = MapUtils.bd09ToWgs84(addressMember[1],addressMember[0]);
                            sheetMember.addCell(new Label(3,memberId,String.valueOf(addressMember[1])));
                            sheetMember.addCell(new Label(4,memberId,String.valueOf(addressMember[0])));
                            sheetMember.addCell(new Label(5,memberId,String.valueOf(addressMember1[0])));
                            sheetMember.addCell(new Label(6,memberId,String.valueOf(addressMember1[1])));
                            sheetMember.addCell(new Label(7,memberId,insertString(member.addressDetail)));
                            sheetMember.addCell(new Label(9,memberId,String.valueOf(member.getWorkPlaceType())));
                            sheetMember.addCell(new Label(10,memberId,insertString(member.sex)));
                            sheetMember.addCell(new Label(11,memberId,String.valueOf(member.age)));
                            sheetMember.addCell(new Label(12,memberId,insertString(member.hukou)));
                            sheetMember.addCell(new Label(13,memberId,TextUtils.isEmpty(member.liveTime)?intUnDone:member.liveTime));
                            sheetMember.addCell(new Label(15,memberId,insertString(member.carrer)));
                            sheetMember.addCell(new Label(16,memberId,insertString(member.studyType)));
                            sheetMember.addCell(new Label(17,memberId,insertString(member.commonTripWay)));
                            sheetMember.addCell(new Label(18,memberId,insertString(member.stopType)));
                            sheetMember.addCell(new Label(19,memberId,insertString(member.stopFee)));
                            sheetMember.addCell(new Label(20,memberId,String.valueOf(member.yidongNum)));
                            sheetMember.addCell(new Label(21,memberId,String.valueOf(member.liantongNum)));
                            sheetMember.addCell(new Label(22,memberId,String.valueOf(member.dianxinNum)));
                            sheetMember.addCell(new Label(23,memberId,member.isLocalNum.equals(textYes)?intDone:intUnDone));
                            sheetMember.addCell(new Label(24,memberId,insertString(member.phoneName)));
                            sheetMember.addCell(new Label(25,memberId,String.valueOf(member.leaveHzTimes)));
                            sheetMember.addCell(new Label(30,memberId,insertString(member.bearMaxTime)));
                            sheetMember.addCell(new Label(31,memberId,TextUtils.isEmpty(member.isInHomeReason)?intUnDone:member.isInHomeReason));

                            List<Trip> trips = member.tripList;
                            if(trips != null && trips.size() > 0){
                                for (int l = 0; l < trips.size(); l++){
                                    Trip trip = trips.get(l);
                                    int tripId = ++ tripLine;
                                    sheetTrip.addCell(new Label(0,tripId,String.valueOf(tripId)));
                                    sheetTrip.addCell(new Label(1,tripId,String.valueOf(l + 1)));
                                    sheetTrip.addCell(new Label(2,tripId,String.valueOf(memberId)));
                                    sheetTrip.addCell(new Label(3,tripId,insertString(trip.aim)));
                                    sheetTrip.addCell(new Label(4,tripId,insertString(trip.outTime)));
                                    double[] addressOut = AddressUtils.getAddressFromString(trip.outAddress);
                                    double[] addressOut1 = MapUtils.bd09ToWgs84(addressOut[1],addressOut[0]);
                                    sheetTrip.addCell(new Label(5,tripId,String.valueOf(addressOut[1])));
                                    sheetTrip.addCell(new Label(6,tripId,String.valueOf(addressOut[0])));
                                    sheetTrip.addCell(new Label(7,tripId,String.valueOf(addressOut1[0])));
                                    sheetTrip.addCell(new Label(8,tripId,String.valueOf(addressOut1[1])));
                                    sheetTrip.addCell(new Label(9,tripId,insertString(trip.outAddressDetail)));
                                    sheetTrip.addCell(new Label(10,tripId,insertString(trip.outAddressType)));
                                    sheetTrip.addCell(new Label(11,tripId,insertString(trip.arriveTime)));
                                    double[] addressArrive = AddressUtils.getAddressFromString(trip.arriveAddress);
                                    double[] addressArrive1 = MapUtils.bd09ToWgs84(addressArrive[1],addressArrive[0]);
                                    sheetTrip.addCell(new Label(12,tripId,String.valueOf(addressArrive[1])));
                                    sheetTrip.addCell(new Label(13,tripId,String.valueOf(addressArrive[0])));
                                    sheetTrip.addCell(new Label(14,tripId,String.valueOf(addressArrive1[0])));
                                    sheetTrip.addCell(new Label(15,tripId,String.valueOf(addressArrive1[1])));
                                    sheetTrip.addCell(new Label(16,tripId,insertString(trip.arriveAddressDetail)));

                                    List<TripWay> tripWays = trip.tripWays;
                                    //交通方式1
                                    if(tripWays != null && tripWays.size() > 0){
                                        TripWay tripWay = tripWays.get(0);
                                        sheetTrip.addCell(new Label(18,tripId,insertString(tripWay.tripWay)));
                                        sheetTrip.addCell(new Label(19,tripId,insertString(tripWay.subWay)));
                                        sheetTrip.addCell(new Label(20,tripId,String.valueOf(tripWay.startWalkTime)));
                                        sheetTrip.addCell(new Label(21,tripId,String.valueOf(tripWay.waitTime)));
                                        sheetTrip.addCell(new Label(22,tripId,insertString(tripWay.stopType)));
                                        sheetTrip.addCell(new Label(23,tripId,String.valueOf(tripWay.stopFee)));

                                        TripWay lastTripWay = tripWays.get(tripWays.size() - 1);
                                        sheetTrip.addCell(new Label(48,tripId,String.valueOf(lastTripWay.endWalkTime)));

                                    }else {
                                        sheetTrip.addCell(new Label(18,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(19,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(20,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(21,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(22,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(23,tripId,intUnDone));

                                        sheetTrip.addCell(new Label(48,tripId,intUnDone));
                                    }
                                    //交通方式2
                                    if(tripWays != null && tripWays.size() > 1){
                                        TripWay tripWay = tripWays.get(1);
                                        sheetTrip.addCell(new Label(24,tripId,insertString(tripWay.tripWay)));
                                        sheetTrip.addCell(new Label(25,tripId,insertString(tripWay.subWay)));
                                        sheetTrip.addCell(new Label(26,tripId,String.valueOf(tripWay.startWalkTime)));
                                        sheetTrip.addCell(new Label(27,tripId,String.valueOf(tripWay.waitTime)));
                                        sheetTrip.addCell(new Label(28,tripId,insertString(tripWay.stopType)));
                                        sheetTrip.addCell(new Label(29,tripId,String.valueOf(tripWay.stopFee)));
                                    }else {
                                        sheetTrip.addCell(new Label(24,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(25,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(26,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(27,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(28,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(29,tripId,intUnDone));


                                    }
                                    //交通方式3
                                    if(tripWays != null && tripWays.size() > 2){
                                        TripWay tripWay = tripWays.get(2);
                                        sheetTrip.addCell(new Label(30,tripId,insertString(tripWay.tripWay)));
                                        sheetTrip.addCell(new Label(31,tripId,insertString(tripWay.subWay)));
                                        sheetTrip.addCell(new Label(32,tripId,String.valueOf(tripWay.startWalkTime)));
                                        sheetTrip.addCell(new Label(33,tripId,String.valueOf(tripWay.waitTime)));
                                        sheetTrip.addCell(new Label(34,tripId,insertString(tripWay.stopType)));
                                        sheetTrip.addCell(new Label(35,tripId,String.valueOf(tripWay.stopFee)));
                                    }else {
                                        sheetTrip.addCell(new Label(30,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(31,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(32,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(33,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(34,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(35,tripId,intUnDone));
                                    }
                                    //交通方式4
                                    if(tripWays != null && tripWays.size() > 3){
                                        TripWay tripWay = tripWays.get(3);
                                        sheetTrip.addCell(new Label(36,tripId,insertString(tripWay.tripWay)));
                                        sheetTrip.addCell(new Label(37,tripId,insertString(tripWay.subWay)));
                                        sheetTrip.addCell(new Label(38,tripId,String.valueOf(tripWay.startWalkTime)));
                                        sheetTrip.addCell(new Label(39,tripId,String.valueOf(tripWay.waitTime)));
                                        sheetTrip.addCell(new Label(40,tripId,insertString(tripWay.stopType)));
                                        sheetTrip.addCell(new Label(41,tripId,String.valueOf(tripWay.stopFee)));
                                    }else {
                                        sheetTrip.addCell(new Label(36,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(37,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(38,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(39,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(40,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(41,tripId,intUnDone));
                                    }
                                    //交通方式5
                                    if(tripWays != null && tripWays.size() > 4){
                                        TripWay tripWay = tripWays.get(4);
                                        sheetTrip.addCell(new Label(42,tripId,insertString(tripWay.tripWay)));
                                        sheetTrip.addCell(new Label(43,tripId,insertString(tripWay.subWay)));
                                        sheetTrip.addCell(new Label(44,tripId,String.valueOf(tripWay.startWalkTime)));
                                        sheetTrip.addCell(new Label(45,tripId,String.valueOf(tripWay.waitTime)));
                                        sheetTrip.addCell(new Label(46,tripId,insertString(tripWay.stopType)));
                                        sheetTrip.addCell(new Label(47,tripId,String.valueOf(tripWay.stopFee)));
                                    }else {
                                        sheetTrip.addCell(new Label(42,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(43,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(44,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(45,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(46,tripId,intUnDone));
                                        sheetTrip.addCell(new Label(47,tripId,intUnDone));
                                    }

                                    sheetTrip.addCell(new Label(49,tripId,String.valueOf(trip.fee)));
                                    sheetTrip.addCell(new Label(50,tripId,String.valueOf(trip.stopFee)));
                                    sheetTrip.addCell(new Label(51,tripId,String.valueOf(trip.outNum)));
                                    sheetTrip.addCell(new Label(52,tripId,trip.isHaveFamily.equals(textYes)?intDone:intUnDone));
                                }
                            }
                        }
                    }
                }
            }
        }

        // 写入数据
        wwb.write();
        // 关闭文件
        wwb.close();

        Toast.makeText(context,"已导出文件"+fileName+".xls到SD卡根目录",Toast.LENGTH_LONG).show();
    }

    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        try {
            font.setColour(Colour.BLUE);// 蓝色字体
        } catch (WriteException e1) {
            e1.printStackTrace();
        }
        WritableCellFormat format = new WritableCellFormat(font);
        try {
            format.setAlignment(jxl.format.Alignment.CENTRE);// 左右居中
            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// 上下居中
            // format.setBorder(Border.ALL, BorderLineStyle.THIN,
            // Colour.BLACK);// 黑色边框
            // format.setBackground(Colour.YELLOW);// 黄色背景
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return format;
    }

    /** 获取SD可用容量 */
    private static long getAvailableStorage() {

        StatFs statFs = new StatFs(root);
        long blockSize = statFs.getBlockSize();
        long availableBlocks = statFs.getAvailableBlocks();
        long availableSize = blockSize * availableBlocks;
        // Formatter.formatFileSize(context, availableSize);
        return availableSize;
    }
}
