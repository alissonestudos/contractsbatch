<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>Altec Coding Style Violations</title>
			</head>
			<style type="text/css">
				body {
				font-family : verdana;
				}
				table {
				border-collapse : collapse;
				border-color : #888888;
				border-style :
				solid;
				border-width : 2px;
				}
			</style>
			<body bgcolor="#FFFFFF">
				<p>
					<b>Coding Style Check Results</b>
				</p>
				<table border="1" cellspacing="0" cellpadding="2">
					<tr bgcolor="#FF2222">
						<th colspan="2">
							<b>
								<font color="#FFFFFF">Summary</font>
							</b>
						</th>
					</tr>
					<tr bgcolor="#DDDDDD">
						<td>Total files checked</td>
						<td>
							<xsl:number level="any"
								value="count(descendant::file)" />
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td>Files with errors</td>
						<td>
							<xsl:number level="any"
								value="count(descendant::file[error])" />
						</td>
					</tr>
					<tr bgcolor="#DDDDDD">
						<td>Total errors</td>
						<td>
							<xsl:number level="any"
								value="count(descendant::error)" />
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td>Errors per file</td>
						<td>
							<xsl:number level="any"
								value="count(descendant::error) div count(descendant::file)" />
						</td>
					</tr>
				</table>
				<hr align="left" width="95%" size="1" />
				<p>The following are violations of the Altec Coding-Style Standards:</p>
				<p />
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>
	<xsl:template match="file[error]">
		<table bgcolor="#DDDDDD" width="95%" border="1" cellspacing="0"
			cellpadding="2">
			<tr>
				<th bgcolor="#B8B8B8" align="left" width="50px">File</th>
				<td bgcolor="#B8B8B8">
					<xsl:value-of select="@name" />
				</td>
			</tr>
			<tr>
				<th align="left" width="50px">
					<font color="#FF2222">Line Number</font>
				</th>
				<th>
					<font color="#FF2222">Error Message</font>
				</th>
			</tr>
			<xsl:apply-templates select="error" />
		</table>
		<p />
	</xsl:template>
	<xsl:template match="error">
		<tr>
			<td bgcolor="#FFFFFF">
				<xsl:value-of select="@line" />
			</td>
			<td bgcolor="#FFFFFF">
				<xsl:value-of select="@message" />
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>