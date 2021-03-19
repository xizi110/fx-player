package xyz.yuelai.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yuelai.View;
import xyz.yuelai.control.SVG;
import xyz.yuelai.domain.MusicInfo;
import xyz.yuelai.domain.SearchResult;
import xyz.yuelai.service.MusicService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchView extends View {

    private static final Logger logger = LoggerFactory.getLogger(SearchView.class);

    private MusicService musicService;
    @FXML
    private TextField keywords;
    @FXML
    private TableView<MusicInfo> tableView;
    @FXML
    private TableColumn<MusicInfo, String> nameColumn;
    @FXML
    private TableColumn<MusicInfo, Integer> indexColumn;

    @Override
    public String fxml() {
        return "/view/SearchPane.fxml";
    }

    public TableView<MusicInfo> getTableView() {
        return tableView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        musicService = new MusicService();

        indexColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<MusicInfo, Integer> call(TableColumn<MusicInfo, Integer> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            TableRow<MusicInfo> tableRow = getTableRow();
                            if (tableRow != null) {
                                int index = tableRow.getIndex();
                                setText(String.valueOf(index + 1));
                            }
                        }
                    }
                };
            }
        });

        nameColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<MusicInfo, String> call(TableColumn<MusicInfo, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            TableRow<MusicInfo> tableRow = getTableRow();
                            MusicInfo musicInfo = tableRow.getItem();
                            setGraphic(null);
                            if (musicInfo != null && musicInfo.getFee() == 1) {
                                SVG svg = readSvgFxml("/assets/icon/vip.fxml");
                                setGraphic(svg);
                                setContentDisplay(ContentDisplay.RIGHT);
                            }
                            setText(item);
                        }
                    }
                };

            }
        });

        tableView.setRowFactory(param -> {
            TableRow<MusicInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    MusicInfo musicInfo = row.getItem();
                    runAsync(new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            logger.debug("当前双击的表格行的 musicInfo：" + musicInfo);
                            String url = musicService.getUrl(musicInfo.getId());
                            logger.debug("获取到的 music url：" + url);
                            if (StringUtils.isNotBlank(url)) {
                                sendMessageWithAsync("mainView:play", url);
                            }
                            return null;
                        }

                        @Override
                        protected void failed() {
                            logger.error(getException().getMessage(), getException());
                        }
                    });
                }
            });
            return row;
        });
    }

    @FXML
    private void search(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && StringUtils.isNotBlank(keywords.getText())) {
            logger.debug("搜索的歌曲名：" + keywords.getText());
            runAsync(new Task<SearchResult>() {
                @Override
                protected SearchResult call() {
                    return musicService.search(keywords.getText());
                }

                @Override
                protected void failed() {
                    logger.error(getException().getMessage(), getException());
                }

                @Override
                protected void updateValue(SearchResult result) {
                    logger.debug("搜索结果：" + result);
                    tableView.getItems().setAll(result.getSongs());
                }
            });
        }
    }

    /**
     * 读取 SVG fxml
     *
     * @param path SVG fxml文件位置
     * @return
     */
    private SVG readSvgFxml(String path) {
        try {
            return FXMLLoader.load(getClass().getResource(path));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
