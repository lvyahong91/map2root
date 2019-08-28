package com.lenovo.map2root;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

     private static String r = "";

    public static void main(String[] args) {
        List<String> roots = new ArrayList<String>();
        Map<String, String> branchRoots = new HashMap<String, String>();
        List<String> parents = new ArrayList<String>();
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        //获取数据处理值
        dataProcess(getData(), roots, branchRoots, parents);
        //计算结果
        for (Map.Entry<String, String> entry : branchRoots.entrySet()) {
            String branch = entry.getKey();
            String col_11 = entry.getValue();
            //判断branch是否是叶子
            if (!parents.contains(branch)) {
                //找col_11的顶层节点
                selectRoot(col_11, roots, branchRoots);
                //将结果存入result集合
                if (result.containsKey(r)) {
                	//将搜索到的根节点r及其对应的basecode放入map类型的result
                    result.get(r).add(branch);
                } else {
                	//新加入根节点r
                    List<String> branchs = new ArrayList<String>();
                    branchs.add(branch);
                    result.put(r, branchs);
                }
            }
            r = "";
        }
        System.out.println("over");
    }

    //递归调用查找root
    private static void selectRoot(String col_11, List<String> roots, Map<String, String> branchRoots) {
        if (roots.contains(col_11)) {
            r = col_11;
        } else {
        	//以col_11作为key,在col_1搜索相等的值对应的新col_11所在的列
            selectRoot(branchRoots.get(col_11), roots, branchRoots);
        }
    }

    //读取csv文件
    private static List<String> getData() {
        List<String> allString = new ArrayList<String>();
        File csv = new File("D:\\360MoveData\\Users\\Administrator\\Desktop\\联想运维工作\\b.csv");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
            String line = "";
            String everyLine = "";
            while ((line = br.readLine()) != null) {
                everyLine = line;
                allString.add(everyLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
    }

    private static void dataProcess(List<String> list, List<String> roots, Map<String, String> branchRoots, List<String> parents) {
        for (String line : list) {
            String[] member = line.split(",");
            if (member.length == 1) {
                roots.add(member[0]);
            } else {
                branchRoots.put(member[0], member[1]);
                parents.add(member[1]);
            }
        }
    }
}