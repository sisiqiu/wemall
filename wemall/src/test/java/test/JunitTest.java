package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fulltl.wemall.modules.wemall.entity.WemallItemActivity;
import com.fulltl.wemall.modules.wemall.service.WemallItemActivityService;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"}) 
@WebAppConfiguration
public class JunitTest {
	@Autowired
	private WemallItemActivityService wemallItemActivityService;
	
	@Test
	public void test() {
		List<WemallItemActivity> findListByItems = wemallItemActivityService.findListByItems(Arrays.asList("6","5"));
		System.err.println(findListByItems.size());
		for(WemallItemActivity entity : findListByItems) {
			System.err.println(entity.getActivity());
		}
	}
}
