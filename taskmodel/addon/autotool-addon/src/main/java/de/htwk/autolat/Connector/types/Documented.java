package de.htwk.autolat.Connector.types;

@SuppressWarnings("unused")
public class Documented<A>
{
    private final A contents;
    private final String documentation;
    
    public Documented(A contents, String documentation)
    {
        this.contents = contents;
        this.documentation = documentation;
    }
    
    public A getContents()
    {
        return contents;
    }
    
    public String getDocumentation()
    {
        return documentation;
    }
    
    @Override
    public String toString()
    {
        return "Documented("
            + contents + ", "
            + documentation + ")";
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (! (other instanceof Documented<?>))
            return false;
        Documented<?> oDocumented = (Documented<?>) other;
        if (!contents.equals(oDocumented.getContents()))
            return false;
        if (!documentation.equals(oDocumented.getDocumentation()))
            return false;
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return
            contents.hashCode() * 1 +
            documentation.hashCode() * 37;
    }
    
}





