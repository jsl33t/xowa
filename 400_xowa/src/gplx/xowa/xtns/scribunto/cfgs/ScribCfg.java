/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012-2020 gnosygnu@gmail.com

XOWA is licensed under the terms of the General Public License (GPL) Version 3,
or alternatively under the terms of the Apache License Version 2.0.

You may use XOWA according to either of these licenses as is most appropriate
for your project on a case-by-case basis.

The terms of each license can be found in the source code repository:

GPLv3 License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-GPLv3.txt
Apache License: https://github.com/gnosygnu/xowa/blob/master/LICENSE-APACHE2.txt
*/
package gplx.xowa.xtns.scribunto.cfgs;

public class ScribCfg {
    public ScribCfg(int timeoutInMs, int sleepInMs, String regexEngine) {
        this.timeoutInMs = timeoutInMs;
        this.sleepInMs = sleepInMs;
        this.regexEngine = regexEngine;
    }
    public int TimeoutInMs() {return timeoutInMs;} private int timeoutInMs;
    public int SleepInMs() {return sleepInMs;} private int sleepInMs;
    public String RegexEngine() {return regexEngine;} private String regexEngine;
}
