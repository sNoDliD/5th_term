import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Knife implements Serializable {
    private int id;
    private String name;
    private Float length;
    private KnifeType knifeType;

    public Knife(String name) {
        this.id = -1;
        this.name = name;
        this.length = -1f;
        this.knifeType = null;
    }

    public Knife(String name, Float length, String knifeTypeName) {
        this.id = -1;
        this.name = name;
        this.length = length;
        this.knifeType = new KnifeType(knifeTypeName);
    }

    public Knife(int id, String name, Float length, KnifeType knifeType) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.knifeType = knifeType;
    }

    public Knife(String name, Float length, KnifeType knifeType) {
        this.name = name;
        this.length = length;
        this.knifeType = knifeType;
    }

    public Knife(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
        this.length = Float.parseFloat(arguments.get(2));
        this.knifeType = new KnifeType(arguments.subList(3, 5));
    }

    public Knife (Element knifeElement,Element knifeTypeElement) {
        this.id = Integer.parseInt(knifeElement.getAttribute("id"));
        this.name = knifeElement.getAttribute("name");
        this.length = Float.parseFloat(knifeElement.getAttribute("length"));
        this.knifeType = new KnifeType(knifeTypeElement);
    }

    public static int listSize() {
        return KnifeType.listSize() + 3;
    }

    public static Knife parseKnife(DataInputStream in) throws IOException {
        List<String> list = new ArrayList<>();

        for (int i = 0, n = Knife.listSize(); i < n; i++)
            list.add(in.readUTF());

        return new Knife(list);
    }

    public KnifeType getKnifeType() {
        return knifeType;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ID:" + id + ", '" + name + "', length : " + length + ", " + knifeType;
    }

    public Element getElement(Document document) {
        Element element = document.createElement("Knife");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);
        element.setAttribute("length", Float.toString(length));

        return element;
    }

    public List<String> toList() {
        List<String> knifeList = new ArrayList<>(List.of(Integer.toString(id), name, Float.toString(length)));
        knifeList.addAll(knifeType.toList());

        return knifeList;
    }

    public Float getLength() {
        return length;
    }

    public String getName() {
        return name;
    }
}
