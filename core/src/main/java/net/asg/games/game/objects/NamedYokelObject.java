package net.asg.games.game.objects;

import org.apache.commons.lang.StringUtils;

public abstract class NamedYokelObject extends AbstractYokelObject {
    private String name;

    public void setName(String name){ this.name = name;}

    public String getName(){ return this.name;}

    private boolean isNameSame(NamedYokelObject namedYokelObject){
        if(namedYokelObject == null) return false;
        return StringUtils.equalsIgnoreCase(name, namedYokelObject.getName());
    }
}
