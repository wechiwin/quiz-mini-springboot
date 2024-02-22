package com.moggi.quizmini;

import com.moggi.quizmini.entity.Folder;
import com.moggi.quizmini.mapper.FolderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author weiqirui
 * @Date 2024/2/22
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FolderTest {

    @Autowired
    FolderMapper folderMapper;

    @Test
    public void addData() {
        Folder folder = new Folder();
        folder.setFoName("Imperativo");
        int insert = folderMapper.insert(folder);
    }


}
