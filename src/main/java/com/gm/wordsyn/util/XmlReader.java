package com.gm.wordsyn.util;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;

@Slf4j
public class XmlReader {
    /**
     * 通过xml文件的地址，打开xml文件，返回xml内容
     *
     * @param xmlFilePath
     * @return
     */
    public String getXmlString(String xmlFilePath) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader bf = new BufferedReader(new FileReader(xmlFilePath));
            String s = null;
            int i = 1;
            while ((s = bf.readLine()) != null) {               //使用readLine方法，一次读一行
                if (i == 1) {
                    i++;
                    continue;
                }
                buffer.append(s.trim());
            }
            String xml = buffer.toString();
            return xml;
        } catch (Exception e) {
            return "error";
        }
    }

    public String getXmlString(byte[] buffer) {
        try {
            String xml = new String(buffer, "utf-8");
            //log.info("xml:: "+xml);
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
