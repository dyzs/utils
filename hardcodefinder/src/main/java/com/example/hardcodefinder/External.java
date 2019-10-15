package com.example.hardcodefinder;

public class External {
    public static void main(String[] args) {
        /*HardcodeFinder finder = new HardcodeFinder();
        finder.operateFind();*/
        try {
            //HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\strings_xml_output_text.txt", "D:\\dyzs_output\\has_been_output_text.txt");


            // 合并iso文件
            // HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\all_data_combine_file.txt", "D:\\dyzs_output\\ios_text_file.txt");

            // HardcodeFinder.twoFilesCompareTheSameText("D:\\dyzs_output\\output_temp.txt", "");

            /*HardcodeFinder.readStringAndOutput(
                    "D:\\dyzs_output\\fileTransfer\\originalTextFile.txt",
                    "D:\\dyzs_output\\fileTransfer\\afterTranslationFile.txt",
                    "D:\\dyzs_output\\fileTransfer\\strings.xml",
                    "D:\\dyzs_output\\fileTransfer\\outputStrings.xml"
            );*/

            HardcodeFinder.readStringEnAndTransferAndOutput(
                    "D:\\dyzs_output\\fileTransferEn\\fileEn.txt",
                    "D:\\dyzs_output\\fileTransferEn\\strings.xml",
                    "D:\\dyzs_output\\fileTransferEn\\outputStrings.xml"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
