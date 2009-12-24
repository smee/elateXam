<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:t="http://complex.taskmodel.thorstenberger.de/complexTaskDef">
 <!-- default rule: copy everything -->
 <xsl:output indent="yes" encoding="UTF-8"/>
 <xsl:template match="node()|@*">
   <xsl:copy>
   <xsl:apply-templates select="@*"/>
   <xsl:apply-templates/>
   </xsl:copy>
 </xsl:template>
 
 <xsl:template match="t:textSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:initialTextFieldValue"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:clozeSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:cloze"/>
    <xsl:copy-of select="t:graphicalCloze"/>
  </xsl:copy>
</xsl:template>
  <xsl:template match="t:mappingSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:concept"/>
    <xsl:copy-of select="t:assignment"/>
  </xsl:copy>
</xsl:template>
  <xsl:template match="t:paintSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:images"/>
    <xsl:copy-of select="t:textualAnswer"/>
    <xsl:copy-of select="t:colorChangeable"/>
    <xsl:copy-of select="t:strokewidthChangeable"/>
  </xsl:copy>
</xsl:template>
  <xsl:template match="t:addonSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:Memento"/>
</xsl:copy>
</xsl:template>
  <xsl:template match="t:mcSubTaskDef">
<xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:problem"/> 
    <xsl:copy-of select="t:hint"/>
    <xsl:copy-of select="t:correctionHint"/>
    <xsl:copy-of select="t:displayedAnswers"/>
    <xsl:copy-of select="t:correct"/>
    <xsl:copy-of select="t:incorrect"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>