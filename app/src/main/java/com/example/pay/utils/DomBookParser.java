package com.example.pay.utils;

import com.example.pay.presenter.WXPayPresenter;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DomBookParser implements BookParser
{

    @Override
    public Map<String, String> parse(InputStream is) throws Exception
    {
        Map<String, String> payParams = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
        DocumentBuilder builder = factory.newDocumentBuilder(); //从factory获取DocumentBuilder实例
        Document doc = builder.parse(is);   //解析输入流 得到Document实例
        NodeList properties = doc.getChildNodes();
        for (int i = 0; i < properties.getLength(); i++)
        {
            Node property = properties.item(i);

            NodeList childNodes = property.getChildNodes();

            for (int j = 0; j < childNodes.getLength(); j++)
            {

                Node node = childNodes.item(j);
                String nodeName = node.getNodeName();
                if (nodeName.equals("return_code"))
                {
                    payParams.put("return_code", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("return_msg"))
                {
                    payParams.put("return_msg", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("appid"))
                {
                    payParams.put("appid", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("mch_id"))
                {
                    payParams.put("mch_id", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("nonce_str"))
                {
                    payParams.put("nonce_str", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("sign"))
                {
                    payParams.put("sign", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("result_code"))
                {
                    payParams.put("result_code", node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("prepay_id"))
                {
                    payParams.put(WXPayPresenter.PAY_PREPAY_ID, node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("trade_type"))
                {
                    payParams.put("trade_type", node.getFirstChild().getNodeValue());
                }
            }
        }
        return payParams;
    }

    @Override
    public String serialize(List<String> books) throws Exception
    {
        return null;
    }

   /* @Override
    public String serialize(List<String> books) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();   //由builder创建新文档

        Element rootElement = doc.createElement("books");

        for (String book : books) {
            Element bookElement = doc.createElement("book");
            bookElement.setAttribute("id", book.getId() + "");

            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(book.getName());
            bookElement.appendChild(nameElement);

            Element priceElement = doc.createElement("price");
            priceElement.setTextContent(book.getPrice() + "");
            bookElement.appendChild(priceElement);

            rootElement.appendChild(bookElement);
        }

        doc.appendChild(rootElement);

        TransformerFactory transFactory = TransformerFactory.newInstance();//取得TransformerFactory实例
        Transformer transformer = transFactory.newTransformer();    //从transFactory获取Transformer实例
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");            // 设置输出采用的编码方式
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");                // 是否自动添加额外的空白
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");   // 是否忽略XML声明

        StringWriter writer = new StringWriter();

        Source source = new DOMSource(doc); //表明文档来源是doc
        Result result = new StreamResult(writer);//表明目标结果为writer
        transformer.transform(source, result);  //开始转换

        return writer.toString();
    }*/

}