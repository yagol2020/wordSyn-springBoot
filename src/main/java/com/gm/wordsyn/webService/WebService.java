package com.gm.wordsyn.webService;

import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.namespace.QName;
import java.util.Vector;

@Slf4j
public class WebService {
    //String serviceUrl = "http://39.107.125.67:4567/WebService1.asmx";
    String serviceUrl = "http://localhost:44342/WebService1.asmx";
    String namespace = "http://tempuri.org/";

    /**
     * 在webService指定的地方，创建文件，占用地方，但没有实质内容
     * 地址是在c#中指定的，这里只要填写文件名
     *
     * @param fileName
     */
    private void createFile(String fileName) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "CreateFile"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "CreateFile");
            call.addParameter(new QName(namespace, "fileName"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_BOOLEAN);//设置返回类型
            Object result = call.invoke(new Object[]{fileName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName
     * @param buffer
     */
    private void appendFileBuffer(String fileName, byte[] buffer) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "Append"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "Append");
            call.addParameter(new QName(namespace, "fileName"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName(namespace, "buffer"), XMLType.SOAP_BASE64BINARY,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_BOOLEAN);//设置返回类型
            Object result = call.invoke(new Object[]{fileName, buffer});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     * @param fileName
     * @return
     */
    public boolean uploadFile(MultipartFile file, String fileName) {
        try {
            createFile(fileName);
            byte[] buffer = file.getBytes();
            appendFileBuffer(fileName, buffer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param fileName
     * @return
     */
    public byte[] downLoadFile(String fileName) {
        byte[] buffer;
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "DownloadFile");         //设置命名空间和需要调用的方法名
            call.setOperationName(qname);                                           
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "DownloadFile");
            call.addParameter(new QName(namespace, "strFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);                    // 接口的参数
            call.setReturnType(XMLType.SOAP_BASE64BINARY);          //设置返回类型
            buffer = (byte[]) call.invoke(new Object[]{fileName});
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 请求webSerivce，分析模板model的word文件，保存为（原名.xml）的文件
     *
     * @param ModelFilePath 文件地址
     */
    public void anyModelFile(String ModelFilePath) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "Word2Xml_model"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "Word2Xml_model");
            call.addParameter(new QName(namespace, "modelFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_STRING);//设置返回类型
            Object result = call.invoke(new Object[]{ModelFilePath});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求webSerivce，分析待检测doc的word文件，保存为（原名.xml）的文件
     *
     * @param DocFilePath 文件地址
     */
    public void anyDocFile(String DocFilePath) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "Word2Xml_doc"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "Word2Xml_doc");
            call.addParameter(new QName(namespace, "docFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_STRING);//设置返回类型
            Object result = call.invoke(new Object[]{DocFilePath});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param modelXmlPath
     * @param DocXmlPath
     */
    public Vector compare2Xml(String modelXmlPath, String DocXmlPath) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "compare2Xml"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "compare2Xml");
            call.addParameter(new QName(namespace, "modelFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName(namespace, "docFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_VECTOR);//设置返回类型
            Vector result = (Vector) call.invoke(new Object[]{modelXmlPath, DocXmlPath});
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "DeleteFile"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "DeleteFile");
            call.addParameter(new QName(namespace, "strFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_STRING);//设置返回类型
            Object result = call.invoke(new Object[]{fileName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param docFilePath
     */
    public void addComment(String docFilePath) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "WriteCommentBetter"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "WriteCommentBetter");
            call.addParameter(new QName(namespace, "docFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_STRING);//设置返回类型
            Object result = call.invoke(new Object[]{docFilePath});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] downloadFile(String fileName) {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            QName qname = new QName(namespace, "DownloadFile"); //设置命名空间和需要调用的方法名
            call.setOperationName(qname);
            call.setTargetEndpointAddress(serviceUrl);
            call.setSOAPActionURI(namespace + "DownloadFile");
            call.addParameter(new QName(namespace, "strFilePath"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(XMLType.SOAP_BASE64BINARY);//设置返回类型
            byte[] result = (byte[]) call.invoke(new Object[]{fileName});
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
