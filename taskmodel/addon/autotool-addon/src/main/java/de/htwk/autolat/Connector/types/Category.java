package de.htwk.autolat.Connector.types;
import java.util.List;

public class Category implements TaskTree
{
    private final String categoryName;
    private final List<TaskTree> subTrees;
    
    public Category(String categoryName, List<TaskTree> subTrees)
    {
        this.categoryName = categoryName;
        this.subTrees = subTrees;
    }
    
    public String getCategoryName()
    {
        return categoryName;
    }
    
    public List<TaskTree> getSubTrees()
    {
        return subTrees;
    }
    
    @Override
    public String toString()
    {
        return "Category("
            + categoryName + ", "
            + subTrees + ")";
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (! (other instanceof Category))
            return false;
        Category oTaskTree = (Category) other;
        if (!categoryName.equals(oTaskTree.getCategoryName()))
            return false;
        if (!subTrees.equals(oTaskTree.getSubTrees()))
            return false;
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return
            categoryName.hashCode() * 1 +
            subTrees.hashCode() * 37;
    }
    
    public Task getTask()
    {
        return null;
    }
    
    public Category getCategory()
    {
        return this;
    }
    
    public boolean isTask()
    {
        return false;
    }
    
    public boolean isCategory()
    {
        return true;
    }
    
}





