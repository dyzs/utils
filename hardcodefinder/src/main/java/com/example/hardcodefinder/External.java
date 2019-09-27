package com.example.hardcodefinder;

public class External {
    public static void main(String[] args) {
        /*HardcodeFinder finder = new HardcodeFinder();
        finder.operateFind();*/
        try {
            //HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\strings_xml_output_text.txt", "D:\\dyzs_output\\has_been_output_text.txt");


            // 合并iso文件
            // HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\all_data_combine_file.txt", "D:\\dyzs_output\\ios_text_file.txt");

            HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\combine_with_ios_file.txt", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
