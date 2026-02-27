package edu.rpi.legup.utility.svg;

import java.awt.*;

import org.w3c.dom.Element;

public abstract class SVGDrawable extends SVGElement {

    protected Stroke stroke;
    protected Paint strokePaint;
    protected Paint fillPaint;

    public SVGDrawable(Element element) {
        super(element);
        float strokeWidth = 1;
        if (element.hasAttribute("stroke-width")) {
            strokeWidth = Float.parseFloat(element.getAttribute("stroke-width"));
        }
        stroke = new BasicStroke(strokeWidth);
        if (element.hasAttribute("stroke")) {
            strokePaint = SVGPaint.getPaint(element.getAttribute("stroke"));
        }
        else {
            strokePaint = new Color(0,0,0,0);
        }
        if (element.hasAttribute("fill")) {
            strokePaint = SVGPaint.getPaint(element.getAttribute("fill"));
        }
        else {
            strokePaint = new Color(0,0,0,0);
        }
    }

    public abstract void draw(Graphics2D g);
}
