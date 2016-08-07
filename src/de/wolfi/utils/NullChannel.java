/*    */ package de.wolfi.utils;

/*    */ import java.net.SocketAddress;

/*    */
/*    */ import io.netty.channel.AbstractChannel;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelConfig;
/*    */ import io.netty.channel.ChannelOutboundBuffer;
/*    */ import io.netty.channel.DefaultChannelConfig;
/*    */ import io.netty.channel.EventLoop;

/*    */
/*    */ public class NullChannel extends AbstractChannel
/*    */ {
	/* 14 */ private final ChannelConfig config = new DefaultChannelConfig(this);

	/*    */
	/*    */ public NullChannel(Channel parent) {
		/* 17 */ super(parent);
		/*    */ }

	/*    */
	/*    */ @Override
	public ChannelConfig config()
	/*    */ {
		/* 22 */ this.config.setAutoRead(true);
		/* 23 */ return this.config;
		/*    */ }

	/*    */
	/*    */ @Override
	protected void doBeginRead()/*    */ throws Exception
	/*    */ {
	}

	/*    */
	/*    */ @Override
	protected void doBind(SocketAddress arg0)/*    */ throws Exception
	/*    */ {
	}

	/*    */
	/*    */ @Override
	protected void doClose()/*    */ throws Exception
	/*    */ {
	}

	/*    */
	/*    */ @Override
	protected void doDisconnect()/*    */ throws Exception
	/*    */ {
	}

	/*    */
	/*    */ @Override
	protected void doWrite(ChannelOutboundBuffer arg0)/*    */ throws Exception
	/*    */ {
	}

	/*    */
	/*    */ @Override
	public boolean isActive()
	/*    */ {
		/* 48 */ return false;
		/*    */ }

	/*    */
	/*    */ @Override
	protected boolean isCompatible(EventLoop arg0)
	/*    */ {
		/* 53 */ return true;
		/*    */ }

	/*    */
	/*    */ @Override
	public boolean isOpen()
	/*    */ {
		/* 58 */ return false;
		/*    */ }

	/*    */
	/*    */ @Override
	protected SocketAddress localAddress0()
	/*    */ {
		/* 63 */ return null;
		/*    */ }

	/*    */
	/*    */ @Override
	public io.netty.channel.ChannelMetadata metadata()
	/*    */ {
		/* 68 */ return null;
		/*    */ }

	/*    */
	/*    */ @Override
	protected AbstractChannel.AbstractUnsafe newUnsafe()
	/*    */ {
		/* 73 */ return null;
		/*    */ }

	/*    */
	/*    */ @Override
	protected SocketAddress remoteAddress0()
	/*    */ {
		/* 78 */ return null;
		/*    */ }
	/*    */ }

/*
 * Location: C:\Users\root\Desktop\Eclispe\APIs\Citizens.jar Qualified Name:
 * net.citizensnpcs.npc.network.EmptyChannel Java Class Version: 6 (50.0)
 * JD-Core Version: 0.7.1
 */