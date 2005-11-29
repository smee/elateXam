<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:t="http://complex.taskmodel.thorstenberger.de/complexTaskHandling">
 <!-- default rule: copy everything -->
 <xsl:output indent="yes" encoding="UTF-8"/>
 <xsl:template match="node()|@*">
   <xsl:copy>
   <xsl:apply-templates select="@*"/>
   <xsl:apply-templates/>
   </xsl:copy>
 </xsl:template>
 
<xsl:template match="t:mcSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:answer"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:clozeSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:gap"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:textSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:answer"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:mappingSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:concept"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:paintSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:pictureString"/>
    <xsl:copy-of select="t:textAnswer"/>
    <xsl:copy-of select="t:undoData"/>
    <xsl:copy-of select="t:resetted"/>
  </xsl:copy>
</xsl:template>
<xsl:template match="t:addonSubTask">
  <xsl:copy>
    <xsl:apply-templates select="@*"/>
    <xsl:copy-of select="t:needsManualCorrection"/> 
    <xsl:copy-of select="t:manualCorrection"/>
    <xsl:copy-of select="t:autoCorrection"/>
    <xsl:copy-of select="t:Memento"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>