<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">

<xsl:template name="shownodes">
  <xsl:param name="query" />
  <xsl:param name="title" />
  <b><xsl:value-of select="$title" /></b>
  <div style="height: 6em; overflow: auto;">
	<code>
	
	
	<xsl:choose>
		<!-- covering (almost) every simple type to simply print via value-of directive -->		
		<xsl:when test="$query castable as xs:boolean">
			<xsl:value-of select="$query"/>
		</xsl:when>				
		<xsl:when test="$query castable as xs:decimal">
			<xsl:value-of select="$query"/>
		</xsl:when>		
		<xsl:when test="$query castable as xs:float">
			<xsl:value-of select="$query"/>
		</xsl:when>		
		<xsl:when test="$query castable as xs:double">
			<xsl:value-of select="$query"/>
		</xsl:when>		
		<xsl:when test="$query castable as xs:string">
			<xsl:choose>		
				<!-- if the resulting string is empty, it's most likely an empy node element,
					 i.e, apply templates -->
				<xsl:when test="string-length(string($query)) = 0">
					<xsl:apply-templates select="$query" />
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="$query"/></xsl:otherwise>
			</xsl:choose>
		</xsl:when>				
		<xsl:otherwise>
			<xsl:choose>		
				<!-- check if query is a node element -->
				<xsl:when test="$query/self::*">
					<xsl:apply-templates select="$query" />
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="$query"/></xsl:otherwise>			
			</xsl:choose>
		</xsl:otherwise>
	</xsl:choose>
	
	
	</code>
  </div>
  <hr/>
</xsl:template>

   <xsl:template match="node()" >
	    <xsl:if test="string-length(name()) != 0">
			<!-- Begin opening tag -->
			<xsl:text>&lt;</xsl:text>
			<xsl:value-of select="name()"/>

		   
			<!-- Attributes -->
			<xsl:for-each select="@*">
				<xsl:text> </xsl:text>
				<xsl:value-of select="name()"/>
				<xsl:text>='</xsl:text>
				<xsl:call-template name="escape-xml">
					<xsl:with-param name="text" select="."/>
				</xsl:call-template>
				<xsl:text>'</xsl:text>
			</xsl:for-each>

			<!-- End opening tag -->
			<xsl:text>&gt;</xsl:text>

			<!-- Content (child elements, text nodes, and PIs) -->
			<xsl:apply-templates select="node()"  />

			<!-- Closing tag -->
			<xsl:text>&lt;/</xsl:text>
			<xsl:value-of select="name()"/>
			<xsl:text>&gt;</xsl:text>
		</xsl:if>
    </xsl:template>

    <xsl:template match="processing-instruction()" >
        <xsl:text>&lt;?</xsl:text>
        <xsl:value-of select="name()"/>
        <xsl:text> </xsl:text>
        <xsl:call-template name="escape-xml">
            <xsl:with-param name="text" select="."/>
        </xsl:call-template>
        <xsl:text>?&gt;</xsl:text>
    </xsl:template>

    <xsl:template name="escape-xml">
        <xsl:param name="text"/>
        <xsl:if test="$text != ''">
            <xsl:variable name="head" select="substring($text, 1, 1)"/>
            <xsl:variable name="tail" select="substring($text, 2)"/>
            <xsl:choose>
                <xsl:when test="$head = '&amp;'">&amp;amp;</xsl:when>
                <xsl:when test="$head = '&lt;'">&amp;lt;</xsl:when>
                <xsl:when test="$head = '&gt;'">&amp;gt;</xsl:when>
                <xsl:when test="$head = '&quot;'">&amp;quot;</xsl:when>
                <xsl:when test="$head = &quot;&apos;&quot;">&amp;apos;</xsl:when>
                <xsl:otherwise><xsl:value-of select="$head"/></xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="escape-xml">
                <xsl:with-param name="text" select="$tail"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
