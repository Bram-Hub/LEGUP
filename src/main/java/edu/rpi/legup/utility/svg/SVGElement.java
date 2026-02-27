package edu.rpi.legup.utility.svg;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

public abstract class SVGElement {

    private String id;
    private ArrayList<String> classes;

    public SVGElement(Element element) {
        if (element.hasAttribute("id")) {
            id = element.getAttribute("id");
        }
        classes = new ArrayList<>();
        if (element.hasAttribute("class")) {
            classes.addAll(List.of(element.getAttribute("class").split(" ")));
        }
    }

    /**
     * Get the id of the element.
     *
     * @return Element id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the classes that the element is a member of.
     *
     * @return Element's class list.
     */
    public ArrayList<String> getClasses() {
        return classes;
    }
}
