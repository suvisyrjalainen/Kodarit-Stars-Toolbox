package com.kodarit.stars.toolbox;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PageTitleTest extends PlaywrightTest {
	@Test
	void testIndexTitle() {
		page.navigate("http://localhost:" + port + "/");
		var title = page.title();
		assertEquals("Stars Toolbox", title);
	}
}
