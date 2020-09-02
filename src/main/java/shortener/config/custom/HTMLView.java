package shortener.config.custom;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HTMLView extends AbstractUrlBasedView {

    private final String htmlDocument;

    public HTMLView(String htmlDocument) {
        this.htmlDocument = htmlDocument;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ServletOutputStream out = httpServletResponse.getOutputStream();
        out.write(this.htmlDocument.getBytes(StandardCharsets.UTF_8));
    }
}
