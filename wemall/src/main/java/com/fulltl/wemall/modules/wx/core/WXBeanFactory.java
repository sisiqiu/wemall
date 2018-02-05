package com.fulltl.wemall.modules.wx.core;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.fulltl.wemall.modules.wx.core.inputHandler.EventInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.ImageInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.LinkInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.LocationInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.ShortVideoInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.TextInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.VideoInputHandler;
import com.fulltl.wemall.modules.wx.core.inputHandler.VoiceInputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.ArticlesOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.ImageOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.LinkOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.MusicOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.TextOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.VideoOutputHandler;
import com.fulltl.wemall.modules.wx.core.outputHandler.VoiceOutputHandler;

@Component
public class WXBeanFactory {
	
	@Resource
	private EventInputHandler eventInputHandler;
	@Resource
	private ImageInputHandler imageInputHandler;
	@Resource
	private LinkInputHandler linkInputHandler;
	@Resource
	private LocationInputHandler locationInputHandler;
	@Resource
	private VideoInputHandler videoInputHandler;
	@Resource
	private ShortVideoInputHandler shortVideoInputHandler;
	@Resource
	private TextInputHandler textInputHandler;
	@Resource
	private VoiceInputHandler voiceInputHandler;
	
	@Resource
	private ArticlesOutputHandler articlesOutputHandler;
	@Resource
	private ImageOutputHandler imageOutputHandler;
	@Resource
	private LinkOutputHandler linkOutputHandler;
	@Resource
	private VideoOutputHandler videoOutputHandler;
	@Resource
	private MusicOutputHandler musicOutputHandler;
	@Resource
	private TextOutputHandler textOutputHandler;
	@Resource
	private VoiceOutputHandler voiceOutputHandler;
	
	/**
	 * 获取事件输入处理类
	 * @return
	 */
	public EventInputHandler getEventInputHandler() {
		return eventInputHandler;
	}
	
	/**
	 * 获取图片输入处理类
	 * @return
	 */
	public ImageInputHandler getImageInputHandler() {
		return imageInputHandler;
	}
	
	/**
	 * 获取链接输入处理类
	 * @return
	 */
	public LinkInputHandler getLinkInputHandler() {
		return linkInputHandler;
	}
	
	/**
	 * 获取位置输入处理类
	 * @return
	 */
	public LocationInputHandler getLocationInputHandler() {
		return locationInputHandler;
	}
	
	/**
	 * 获取视频输入处理类
	 * @return
	 */
	public VideoInputHandler getVideoInputHandler() {
		return videoInputHandler;
	}
	
	/**
	 * 获取短视频输入处理类
	 * @return
	 */
	public ShortVideoInputHandler getShortVideoInputHandler() {
		return shortVideoInputHandler;
	}
	
	/**
	 * 获取文本输入处理类
	 * @return
	 */
	public TextInputHandler getTextInputHandler() {
		return textInputHandler;
	}
	
	/**
	 * 获取语音输入处理类
	 * @return
	 */
	public VoiceInputHandler getVoiceInputHandler() {
		return voiceInputHandler;
	}
	
	/**
	 * 获取图文输出处理类
	 * @return
	 */
	public ArticlesOutputHandler getArticlesOutputHandler() {
		return articlesOutputHandler;
	}
	
	/**
	 * 获取图片输出处理类
	 * @return
	 */
	public ImageOutputHandler getImageOutputHandler() {
		return imageOutputHandler;
	}
	
	/**
	 * 获取链接输出处理类
	 * @return
	 */
	public LinkOutputHandler getLinkOutputHandler() {
		return linkOutputHandler;
	}

	/**
	 * 获取视频输出处理类
	 * @return
	 */
	public VideoOutputHandler getVideoOutputHandler() {
		return videoOutputHandler;
	}

	/**
	 * 获取音乐输出处理类
	 * @return
	 */
	public MusicOutputHandler getMusicOutputHandler() {
		return musicOutputHandler;
	}

	/**
	 * 获取文本输出处理类
	 * @return
	 */
	public TextOutputHandler getTextOutputHandler() {
		return textOutputHandler;
	}

	/**
	 * 获取语音输出处理类
	 * @return
	 */
	public VoiceOutputHandler getVoiceOutputHandler() {
		return voiceOutputHandler;
	}
	
}
