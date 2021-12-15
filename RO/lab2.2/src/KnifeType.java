import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KnifeType implements Serializable {
    private int id;
    private String name;

    public KnifeType(String name) {
        this.id = -1;
        this.name = name;
    }

    public KnifeType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public KnifeType(Element knifeTypeElement) {
        this.id = Integer.parseInt(knifeTypeElement.getAttribute("id"));
        this.name = knifeTypeElement.getAttribute("name");
    }

    public KnifeType(List<String> arguments) {
        this.id = Integer.parseInt(arguments.get(0));
        this.name = arguments.get(1);
    }

    public static int listSize() {
        return 2;
    }

    public static KnifeType parseKnifeType(DataInputStream in) throws IOException {
        List<String> list = new ArrayList<>();

        for (int i = 0, n = KnifeType.listSize(); i < n; i++)
            list.add(in.readUTF());

        return new KnifeType(list);
    }

    public List<String> toList() {
        return new ArrayList<>(List.of(Integer.toString(id), name));
    }

    @Override
    public String toString() {
        return "KnifeType ID:" + id + ", " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnifeType knifeType = (KnifeType) o;
        return id == knifeType.id && Objects.equals(name, knifeType.name);
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Element getElement(Document document) {
        Element element = document.createElement("KnifeType");

        element.setAttribute("id", Integer.toString(id));
        element.setAttribute("name", name);

        return element;
    }

    public String getName() {
        return name;
    }
}
