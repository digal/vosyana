package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 12:14:11
 */
public class ONNNEntry implements IONNNEntry {
	private String voterId;
    private String targetId;
    private String chatId;

    private int id;
    private long timestamp;


    public ONNNEntry(String voter, String target, String chatId)
	{
        this.voterId = voter;
        this.targetId = target;
        this.chatId = chatId;
    }

	public String getTargetId()
	{
		return targetId;
	}

    public String getVoterId()
	{
		return voterId;
	}

    public String getChatId() {
        return chatId;
    }

    @Override
	public boolean equals(Object o) {
		if (o instanceof ONNNEntry) {
			ONNNEntry e2 = (ONNNEntry)o;
			return getVoterId().equals(e2.getVoterId()) && getTargetId().equals(e2.getTargetId()) && getChatId().equals(e2.getChatId());
		}
		return super.equals(o);
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
