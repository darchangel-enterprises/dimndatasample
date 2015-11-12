package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(namespace="http://www.opsource.net/simpleapp")
public class Server
{
	int id;

	String name;

    public Server(){}

	//public Server(int id, String name ){
	//	this.id = id;
	//	this.name = name;
	//}

	public void setId(int id){
		this.id = id;
	}

	public void setName(String name ){
		this.name =  "'"  + name + "'";
	}

    @XmlElement
	public int getId(){
	  return id;
	}

    @XmlElement
	public String getName(){
	  return name;
	}
}