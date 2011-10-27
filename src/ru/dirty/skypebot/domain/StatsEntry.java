package ru.dirty.skypebot.domain;

import java.util.StringTokenizer;
import java.sql.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Digal
 * Date: 25.09.2008
 * Time: 18:07:04
 * To change this template use File | Settings | File Templates.
 */
public class StatsEntry extends BaseDomain{
    private static final String DELIM_WORD = " \t\n,.!?()\"";

    private String chatID;
    private String userID;
    private Date started;
    private long messages;
    private long commands;
    private long symbols;
    private long words;
    private long topicChanges;
    //private long emoticons;

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }

    public void setCommands(long commands) {
        this.commands = commands;
    }

    public void setSymbols(long symbols) {
        this.symbols = symbols;
    }

    public void setWords(long words) {
        this.words = words;
    }

    public void setTopicChanges(long topicChanges) {
        this.topicChanges = topicChanges;
    }


    public StatsEntry(String chatID, String userID) {
        this.chatID = chatID;
        this.userID = userID;
        started = null;
    }

    public synchronized void apply(String message) {
        if (message!=null) {
            if (message.startsWith("!")) {
                commands++;
            } else {
                messages++;
                symbols+=message.length();
                words+=getWordCount(message);
            }
        }
    }

    public synchronized void countTopicChange() {
        topicChanges++;
    }

    private int getWordCount(String message) {
        StringTokenizer stn = new StringTokenizer(message, DELIM_WORD);
        return stn.countTokens();
    }

    public String getChatID() {
        return chatID;
    }

    public String getUserID() {
        return userID;
    }

    public long getMessages() {
        return messages;
    }

    public long getCommands() {
        return commands;
    }

    public long getSymbols() {
        return symbols;
    }

    public long getWords() {
        return words;
    }

    public long getTopicChanges() {
        return topicChanges;
    }

    public Date getStarted() {
        return started;
    }

    //
//    public long getEmoticons() {
//        return emoticons;
//    }


    /*
      [13:42:07] Berkus: pcrecpp::RE smilie_regexp("(\\B(:\">|:&|:=&|:=@|:=\\#|:=\\$|:=\\(|:=\\)|:=\\*|:=\\?|:=\\||:@|:\\#|:\\$|:\\(|:\\)|:\\*|:\\-&|:\\-@|:\\-\\#|:\\-\\$|:\\-\\(|:\\-\\)|:\\-\\*|:\\-\\?|:\\-\\||:\\?|:\\^\\)|:\\||;=\\(|;=\\)|;\\(|;\\)|;\\-\\(|;\\-\\)|>:\\)|\\(:\\||\\(\\$\\)|\\(\\*\\)|\\(\\^\\)|\\(angel\\)|\\(angry\\)|\\(bandit\\)|\\(banghead\\)|\\(bear\\)|\\(beer\\)|\\(blush\\)|\\(bow\\)|\\(bricklayers\\)|\\(brokenheart\\)|\\(bug\\)|\\(bye\\)|\\(cake\\)|\\(call\\)|\\(cash\\)|\\(chuckle\\)|\\(ci\\)|\\(clap\\)|\\(clock\\)|\\(coffee\\)|\\(cool\\)|\\(cry\\)|\\(d\\)|\\(d\\)|\\(dance\\)|\\(devil\\)|\\(doh\\)|\\(drink\\)|\\(drunk\\)|\\(dull\\)|\\(e\\)|\\(emo\\)|\\(envy\\)|\\(f\\)|\\(f\\)|\\(film\\)|\\(finger\\)|\\(flag:ad\\)|\\(flag:ae\\)|\\(flag:af\\)|\\(flag:ag\\)|\\(flag:ai\\)|\\(flag:al\\)|\\(flag:am\\)|\\(flag:an\\)|\\(flag:ao\\)|\\(flag:aq\\)|\\(flag:ar\\)|\\(flag:as\\)|\\(flag:at\\)|\\(flag:au\\)|\\(flag:aw\\)|\\(flag:ax\\)|\\(flag:az\\)|\\(flag:ba\\)|\\(flag:bb\\)|\\(flag:bd\\)|\\(flag:be\\)|\\(flag:bf\\)|\\(flag:bg\\)|\\(flag:bh\\)|\\(flag:bi\\)|\\(flag:bj\\)|\\(flag:bm\\)|\\(flag:bn\\)|\\(flag:bo\\)|\\(flag:br\\)|\\(flag:bs\\)|\\(flag:bt\\)|\\(flag:bv\\)|\\(flag:bw\\)|\\(flag:by\\)|\\(flag:bz\\)|\\(flag:ca\\)|\\(flag:cc\\)|\\(flag:cd\\)|\\(flag:cf\\)|\\(flag:cg\\)|\\(flag:ch\\)|\\(flag:ci\\)|\\(flag:ck\\)|\\(flag:cl\\)|\\(flag:cm\\)|\\(flag:cn\\)|\\(flag:co\\)|\\(flag:cr\\)|\\(flag:cu\\)|\\(flag:cv\\)|\\(flag:cx\\)|\\(flag:cy\\)|\\(flag:cz\\)|\\(flag:de\\)|\\(flag:dj\\)|\\(flag:dk\\)|\\(flag:dm\\)|\\(flag:do\\)|\\(flag:dz\\)|\\(flag:ec\\)|\\(flag:ee\\)|\\(flag:eg\\)|\\(flag:eh\\)|\\(flag:er\\)|\\(flag:es\\)|\\(flag:et\\)|\\(flag:eu\\)|\\(flag:fi\\)|\\(flag:fj\\)|\\(flag:fk\\)|\\(flag:fm\\)|\\(flag:fo\\)|\\(flag:fr\\)|\\(flag:ga\\)|\\(flag:gb\\)|\\(flag:gd\\)|\\(flag:ge\\)|\\(flag:gf\\)|\\(flag:gh\\)|\\(flag:gi\\)|\\(flag:gl\\)|\\(flag:gm\\)|\\(flag:gn\\)|\\(flag:gp\\)|\\(flag:gq\\)|\\(flag:gr\\)|\\(flag:gs\\)|\\(flag:gt\\)|\\(flag:gu\\)|\\(flag:gw\\)|\\(flag:gy\\)|\\(flag:hk\\)|\\(flag:hm\\)|\\(flag:hn\\)|\\(flag:hr\\)|\\(flag:ht\\)|\\(flag:hu\\)|\\(flag:id\\)|\\(flag:ie\\)|\\(flag:il\\)|\\(flag:in\\)|\\(flag:io\\)|\\(flag:iq\\)|\\(flag:ir\\)|\\(flag:is\\)|\\(flag:it\\)|\\(flag:jm\\)|\\(flag:jo\\)|\\(flag:jp\\)|\\(flag:ke\\)|\\(flag:kg\\)|\\(flag:kh\\)|\\(flag:ki\\)|\\(flag:km\\)|\\(flag:kn\\)|\\(flag:kp\\)|\\(flag:kr\\)|\\(flag:kw\\)|\\(flag:ky\\)|\\(flag:kz\\)|\\(flag:la\\)|\\(flag:lb\\)|\\(flag:lc\\)|\\(flag:li\\)|\\(flag:lk\\)|\\(flag:lr\\)|\\(flag:ls\\)|\\(flag:lt\\)|\\(flag:lu\\)|\\(flag:lv\\)|\\(flag:ly\\)|\\(flag:ma\\)|\\(flag:mc\\)|\\(flag:md\\)|\\(flag:me\\)|\\(flag:mg\\)|\\(flag:mh\\)|\\(flag:mk\\)|\\(flag:ml\\)|\\(flag:mm\\)|\\(flag:mn\\)|\\(flag:mo\\)|\\(flag:mp\\)|\\(flag:mq\\)|\\(flag:mr\\)|\\(flag:ms\\)|\\(flag:mt\\)|\\(flag:mu\\)|\\(flag:mv\\)|\\(flag:mw\\)|\\(flag:mx\\)|\\(flag:my\\)|\\(flag:mz\\)|\\(flag:na\\)|\\(flag:nc\\)|\\(flag:ne\\)|\\(flag:nf\\)|\\(flag:ng\\)|\\(flag:ni\\)|\\(flag:nl\\)|\\(flag:no\\)|\\(flag:np\\)|\\(flag:nr\\)|\\(flag:nu\\)|\\(flag:nz\\)|\\(flag:om\\)|\\(flag:pa\\)|\\(flag:pe\\)|\\(flag:pf\\)|\\(flag:pg\\)|\\(flag:ph\\)|\\(flag:pk\\)|\\(flag:pl\\)|\\(flag:pm\\)|\\(flag:pn\\)|\\(flag:pr\\)|\\(flag:ps\\)|\\(flag:pt\\)|\\(flag:pw\\)|\\(flag:py\\)|\\(flag:qa\\)|\\(flag:re\\)|\\(flag:ro\\)|\\(flag:rs\\)|\\(flag:ru\\)|\\(flag:rw\\)|\\(flag:sa\\)|\\(flag:sb\\)|\\(flag:sc\\)|\\(flag:sd\\)|\\(flag:se\\)|\\(flag:sg\\)|\\(flag:sh\\)|\\(flag:si\\)|\\(flag:sj\\)|\\(flag:sk\\)|\\(flag:sl\\)|\\(flag:sm\\)|\\(flag:sn\\)|\\(flag:so\\)|\\(flag:sr\\)|\\(flag:ss\\)|\\(flag:st\\)|\\(flag:sv\\)|\\(flag:sy\\)|\\(flag:sz\\)|\\(flag:tc\\)|\\(flag:td\\)|\\(flag:tf\\)|\\(flag:tg\\)|\\(flag:th\\)|\\(flag:tj\\)|\\(flag:tk\\)|\\(flag:tl\\)|\\(flag:tm\\)|\\(flag:tn\\)|\\(flag:to\\)|\\(flag:tr\\)|\\(flag:tt\\)|\\(flag:tv\\)|\\(flag:tw\\)|\\(flag:tz\\)|\\(flag:ua\\)|\\(flag:ug\\)|\\(flag:uk\\)|\\(flag:um\\)|\\(flag:us\\)|\\(flag:uy\\)|\\(flag:uz\\)|\\(flag:va\\)|\\(flag:vc\\)|\\(flag:ve\\)|\\(flag:vg\\)|\\(flag:vi\\)|\\(flag:vn\\)|\\(flag:vu\\)|\\(flag:wf\\)|\\(flag:ws\\)|\\(flag:ye\\)|\\(flag:yt\\)|\\(flag:za\\)|\\(flag:zm\\)|\\(flag:zw\\)|\\(flex\\)|\\(flower\\)|\\(fubar\\)|\\(giggle\\)|\\(grin\\)|\\(h\\)|\\(h\\)|\\(handshake\\)|\\(happy\\)|\\(headbang\\)|\\(heart\\)|\\(heidy\\)|\\(hi\\)|\\(hrv\\)|\\(hug\\)|\\(inlove\\)|\\(kate\\)|\\(kiss\\)|\\(l\\)|\\(l\\)|\\(laugh\\)|\\(london\\)|\\(love\\)|\\(m\\)|\\(mail\\)|\\(makeup\\)|\\(mm\\)|\\(mmm\\)|\\(mmmm\\)|\\(mo\\)|\\(mooning\\)|\\(movie\\)|\\(mp\\)|\\(muscle\\)|\\(music\\)|\\(myspace\\)|\\(n\\)|\\(n\\)|\\(nerd\\)|\\(ninja\\)|\\(no\\)|\\(nod\\)|\\(o\\)|\\(o\\)|\\(ok\\)|\\(party\\)|\\(ph\\)|\\(phone\\)|\\(pi\\)|\\(pizza\\)|\\(poolparty\\)|\\(puke\\)|\\(punch\\)|\\(rain\\)|\\(rock\\)|\\(rofl\\)|\\(sad\\)|\\(shake\\)|\\(skype\\)|\\(smile\\)|\\(smirk\\)|\\(smoke\\)|\\(smoking\\)|\\(snooze\\)|\\(speechless\\)|\\(ss\\)|\\(star\\)|\\(sun\\)|\\(surprised\\)|\\(swear\\)|\\(sweat\\)|\\(talk\\)|\\(think\\)|\\(time\\)|\\(tmi\\)|\\(toivo\\)|\\(tongueout\\)|\\(u\\)|\\(u\\)|\\(wait\\)|\\(wasntme\\)|\\(wave\\)|\\(whew\\)|\\(wink\\)|\\(wonder\\)|\\(worry\\)|\\(y\\)|\\(y\\)|\\(yawn\\)|\\(yes\\)|\\(~\\)|\\\\:d/|\\\\:d/|\\\\o/|\\]:\\)|\\|=\\(|\\|\\(|\\|\\-\\(|\\|\\-\\))\\B|(?<=\\W|^)(8=\\)|8=\\||8\\-\\)|8\\-\\||8\\||b=\\)|b=\\||b\\-\\)|b\\-\\||b\\||i=\\)|i\\-\\)|x=\\(|x=\\(|x\\(|x\\(|x\\-\\(|x\\-\\()|(:=d|:=d|:=o|:=o|:=p|:=p|:=s|:=s|:=x|:=x|:\\-d|:\\-d|:\\-o|:\\-o|:\\-p|:\\-p|:\\-s|:\\-s|:\\-x|:\\-x|:d|:d|:o|:o|:p|:p|:s|:s|:x|:x)\\b)", pcrecpp::RE_Options().set_utf8(true).set_caseless(true));


     */
}

