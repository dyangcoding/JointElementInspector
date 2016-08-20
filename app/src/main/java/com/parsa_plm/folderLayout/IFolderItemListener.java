package com.parsa_plm.folderLayout;

import java.io.File;

public interface IFolderItemListener {
    void OnCannotFileRead(File file);//implement what to do folder is Unreadable
    void OnFileClicked(File file);//What to do When a file is clicked
}
