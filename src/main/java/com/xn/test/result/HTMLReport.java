package com.xn.test.result;/**
 * Created by xn056839 on 2016/9/12.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLReport {
    private static final Logger logger = LoggerFactory.getLogger(HTMLReport.class);
    private static String HTML_TMPL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <title>%(title)s</title>\n" +
            "    <meta name=\"generator\" content=\"%(generator)s\"/>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
            "    %(stylesheet)s\n" +
            "</head>\n" +
            "<body>\n" +
            "<script language=\"javascript\" type=\"text/javascript\"><!--\n" +
            "output_list = Array();\n" +
            "\n" +
            "/* level - 0:Summary; 1:Failed; 2:All */\n" +
            "function showCase(level) {\n" +
            "    trs = document.getElementsByTagName(\"tr\");\n" +
            "    for (var i = 0; i < trs.length; i++) {\n" +
            "        tr = trs[i];\n" +
            "        id = tr.id;\n" +
            "        if (id.substr(0,2) == 'ft') {\n" +
            "            if (level < 1) {\n" +
            "                tr.className = 'hiddenRow';\n" +
            "            }\n" +
            "            else {\n" +
            "                tr.className = '';\n" +
            "            }\n" +
            "        }\n" +
            "        if (id.substr(0,2) == 'pt') {\n" +
            "            if (level > 1) {\n" +
            "                tr.className = '';\n" +
            "            }\n" +
            "            else {\n" +
            "                tr.className = 'hiddenRow';\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "function showClassDetail(cid, count) {\n" +
            "    var id_list = Array(count);\n" +
            "    var toHide = 1;\n" +
            "    for (var i = 0; i < count; i++) {\n" +
            "        tid0 = 't' + cid.substr(1) + '.' + (i+1);\n" +
            "        tid = 'f' + tid0;\n" +
            "        tr = document.getElementById(tid);\n" +
            "        if (!tr) {\n" +
            "            tid = 'p' + tid0;\n" +
            "            tr = document.getElementById(tid);\n" +
            "        }\n" +
            "        id_list[i] = tid;\n" +
            "        if (tr.className) {\n" +
            "            toHide = 0;\n" +
            "        }\n" +
            "    }\n" +
            "    for (var i = 0; i < count; i++) {\n" +
            "        tid = id_list[i];\n" +
            "        if (toHide) {\n" +
            "            document.getElementById('div_'+tid).style.display = 'none'\n" +
            "            document.getElementById(tid).className = 'hiddenRow';\n" +
            "        }\n" +
            "        else {\n" +
            "            document.getElementById(tid).className = '';\n" +
            "        }\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "function showTestDetail(div_id){\n" +
            "    var details_div = document.getElementById(div_id)\n" +
            "    var displayState = details_div.style.display\n" +
            "    // alert(displayState)\n" +
            "    if (displayState != 'block' ) {\n" +
            "        displayState = 'block'\n" +
            "        details_div.style.display = 'block'\n" +
            "    }\n" +
            "    else {\n" +
            "        details_div.style.display = 'none'\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "\n" +
            "function html_escape(s) {\n" +
            "    s = s.replace(/&/g,'&amp;');\n" +
            "    s = s.replace(/</g,'&lt;');\n" +
            "    s = s.replace(/>/g,'&gt;');\n" +
            "    return s;\n" +
            "}\n" +
            "\n" +
            "/* obsoleted by detail in <div>\n" +
            "function showOutput(id, name) {\n" +
            "    var w = window.open(\"\", //url\n" +
            "                    name,\n" +
            "                    \"resizable,scrollbars,status,width=800,height=450\");\n" +
            "    d = w.document;\n" +
            "    d.write(\"<pre>\");\n" +
            "    d.write(html_escape(output_list[id]));\n" +
            "    d.write(\"\\n\");\n" +
            "    d.write(\"<a href='javascript:window.close()'>close</a>\\n\");\n" +
            "    d.write(\"</pre>\\n\");\n" +
            "    d.close();\n" +
            "}\n" +
            "*/\n" +
            "--></script>\n" +
            "\n" +
            "%(heading)s\n" +
            "%(report)s\n" +
            "%(ending)s\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    public static String STYLESHEET_TMPL = "<style type=\"text/css\" media=\"screen\">\n" +
            "body        { font-family: verdana, arial, helvetica, sans-serif; font-size: 80%; }\n" +
            "table       { font-size: 100%; }\n" +
            "pre         { }\n" +
            "\n" +
            "/* -- heading ---------------------------------------------------------------------- */\n" +
            "h1 {\n" +
            "\tfont-size: 16pt;\n" +
            "\tcolor: gray;\n" +
            "}\n" +
            ".heading {\n" +
            "    margin-top: 0ex;\n" +
            "    margin-bottom: 1ex;\n" +
            "}\n" +
            "\n" +
            ".heading .attribute {\n" +
            "    margin-top: 1ex;\n" +
            "    margin-bottom: 0;\n" +
            "}\n" +
            "\n" +
            ".heading .description {\n" +
            "    margin-top: 4ex;\n" +
            "    margin-bottom: 6ex;\n" +
            "}\n" +
            "\n" +
            "/* -- css div popup ------------------------------------------------------------------------ */\n" +
            "a.popup_link {\n" +
            "}\n" +
            "\n" +
            "a.popup_link:hover {\n" +
            "    color: red;\n" +
            "}\n" +
            "\n" +
            ".popup_window {\n" +
            "    display: none;\n" +
            "    position: relative;\n" +
            "    left: 0px;\n" +
            "    top: 0px;\n" +
            "    /*border: solid #627173 1px; */\n" +
            "    padding: 10px;\n" +
            "    background-color: #E6E6D6;\n" +
            "    font-family: \"Lucida Console\", \"Courier New\", Courier, monospace;\n" +
            "    text-align: left;\n" +
            "    font-size: 10pt;\n" +
            "    width: 800px;\n" +
            "}\n" +
            "\n" +
            "}\n" +
            "/* -- report ------------------------------------------------------------------------ */\n" +
            "#show_detail_line {\n" +
            "    margin-top: 3ex;\n" +
            "    margin-bottom: 1ex;\n" +
            "}\n" +
            "#result_table {\n" +
            "    width: 80%;\n" +
            "    border-collapse: collapse;\n" +
            "    border: 1px solid #777;\n" +
            "}\n" +
            "#header_row {\n" +
            "    font-weight: bold;\n" +
            "    color: white;\n" +
            "    background-color: #777;\n" +
            "}\n" +
            "#result_table td {\n" +
            "    border: 1px solid #777;\n" +
            "    padding: 2px;\n" +
            "}\n" +
            "#total_row  { font-weight: bold; }\n" +
            ".passClass  { background-color: #6c6; }\n" +
            ".failClass  { background-color: #c60; }\n" +
            ".errorClass { background-color: #c00; }\n" +
            ".passCase   { color: #6c6; }\n" +
            ".failCase   { color: #c60; font-weight: bold; }\n" +
            ".errorCase  { color: #c00; font-weight: bold; }\n" +
            ".hiddenRow  { display: none; }\n" +
            ".testcase   { margin-left: 2em; }\n" +
            ".testrange { margin-left: 0.4em; }\n" +
            "\n" +
            "/* -- ending ---------------------------------------------------------------------- */\n" +
            "#ending {\n" +
            "}\n" +
            "\n" +
            "</style>";
    public static String TEST_TMPL = "<div class='heading'>\n" +
            "<h1>%(title)s</h1>\n" +
            "    <table id='result_table'>\n" +
            "<tr id='header_row'>\n" +
            "<td><b>测试环境</b></td>\n" +
            "<td><div class='testrange'>%(test_environment)s</div></td>\n" +
            "</tr>\n" +
            "\n" +
            "</table></div></br>";
    public static String HEADING_TMPL = "<div class='heading'>\n" +
            "%(parameters)s\n" +
            "<p class='description'>%(description)s</p>\n" +
            "</div>";

    public static String HEADING_ATTRIBUTE_TMPL="<p class='attribute'><strong>%(name)s:</strong> %(value)s</p>";
    public static String REPORT_TMPL="<p id='show_detail_line'>Show\n" +
            "<a href='javascript:showCase(0)'>Summary</a>\n" +
            "<a href='javascript:showCase(1)'>Failed</a>\n" +
            "<a href='javascript:showCase(2)'>All</a>\n" +
            "</p>\n" +
            "<table id='result_table'>\n" +
            "<colgroup>\n" +
            "<col align='left' />\n" +
            "<col align='right' />\n" +
            "<col align='right' />\n" +
            "<col align='right' />\n" +
            "<col align='right' />\n" +
            "<col align='right' />\n" +
            "</colgroup>\n" +
            "<tr id='header_row'>\n" +
            "    <td>Test Group/Test case</td>\n" +
            "    <td>Count</td>\n" +
            "    <td>Pass</td>\n" +
            "    <td>Fail</td>\n" +
            "    <td>Error</td>\n" +
            "    <td>View</td>\n" +
            "</tr>\n" +
            "%(test_list)s\n" +
            "<tr id='total_row'>\n" +
            "    <td>Total</td>\n" +
            "    <td>%(count)s</td>\n" +
            "    <td>%(Pass)s</td>\n" +
            "    <td>%(fail)s</td>\n" +
            "    <td>%(error)s</td>\n" +
            "    <td>&nbsp;</td>\n" +
            "</tr>\n" +
            "</table>";

    public static String REPORT_CLASS_TMPL="<tr class='%(style)s'>\n" +
            "    <td>%(desc)s</td>\n" +
            "    <td>%(count)s</td>\n" +
            "    <td>%(Pass)s</td>\n" +
            "    <td>%(fail)s</td>\n" +
            "    <td>%(error)s</td>\n" +
            "    <td><a href=\"javascript:showClassDetail('%(cid)s',%(count)s)\">Detail</a></td>\n" +
            "</tr>\n";
    public static String REPORT_TEST_WITH_OUTPUT_TMPL="<tr id='%(tid)s' class='%(Class)s'>\n" +
            "    <td class='%(style)s'><div class='testcase'>%(desc)s</div></td>\n" +
            "    <td colspan='5' align='center'>\n" +
            "\n" +
            "    <!--css div popup start-->\n" +
            "    <a class=\"popup_link\" onfocus='this.blur();' href=\"javascript:showTestDetail('div_%(tid)s')\" >\n" +
            "        %(status)s</a>\n" +
            "\n" +
            "    <div id='div_%(tid)s' class=\"popup_window\">\n" +
            "        <div style='text-align: right; color:red;cursor:pointer'>\n" +
            "        <a onfocus='this.blur();' onclick=\"document.getElementById('div_%(tid)s').style.display = 'none' \" >\n" +
            "           [x]</a>\n" +
            "        </div>\n" +
            "        <pre style=\"white-space:pre-wrap;word-wrap:break-word\">\n" +
            "        %(script)s\n" +
            "        </pre>\n" +
            "    </div>\n" +
            "    <!--css div popup end-->\n" +
            "\n" +
            "    </td>\n" +
            "</tr>";
    public static String REPORT_TEST_NO_OUTPUT_TMPL="<tr id='%(tid)s' class='%(Class)s'>\n" +
            "    <td class='%(style)s'><div class='testcase'>%(desc)s</div></td>\n" +
            "    <td colspan='5' align='center'>%(status)s</td>\n" +
            "</tr>";
    public  static String REPORT_TEST_OUTPUT_TMPL="REPORT_TEST_OUTPUT_TMPL";
    public  static  String ENDING_TMPL="<div id='ending'>&nbsp;</div>";
}
