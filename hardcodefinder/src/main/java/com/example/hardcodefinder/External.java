package com.example.hardcodefinder;

import com.example.aes.AESUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class External {
    public static void main(String[] args) {
        if (true)return;
        try {
            String encryptStr = "P2FjEzj8MgEVpZ8hbZrWoBEs1J93lBJJ0la6OpkffY1AVWNAMkNJfb92zJ2mY10dR64dEHo0SNY2DOr2GxpvFT3c1vWU60EGyHvxm1ZHudXCv342o88ipID027m0U9wPgFb29eTeGUvUCeqeczPhGWL5JWAgwiHDsGA8RlNCp%2Fwq+910B+6isVn2n5fWcmVrz9gGDT6bqQoBeWvPf41a%2FHFhQVp+EyBhsuGq6za9I7kGO%2FtypYb6uRmf3Kn%2FS%2FA1XYXFA+btkVhnqyZSQXHHe3ubC4Ytf9aSdWm0Uas4fBaVsO4k53f6Wru531rSviPs2TK9t9Gqx9B9P8hVKbdp2mYFwhL4553FteHundJ+8bH%2Fv9FVUF0JuqX6z%2F5iUqtrJmSgeuLZzXNoOVvKKxBRtw%3D%3D";



            String encryptStr2 = "P2FjEzj8MgEVpZ8hbZrWoBEs1J93lBJJ0la6OpkffY1AVWNAMkNJfb92zJ2mY10dR64dEHo0SNY2DOr2GxpvFT3c1vWU60EGyHvxm1ZHudXCv342o88ipID027m0U9wPgFb29eTeGUvUCeqeczPhGWL5JWAgwiHDsGA8RlNCp%2Fwq+910B+6isVn2n5fWcmVrz9gGDT6bqQoBeWvPf41a%2FHFhQVp+EyBhsuGq6za9I7kGO%2FtypYb6uRmf3Kn%2FS%2FA1XYXFA+btkVhnqyZSQXHHe3ubC4Ytf9aSdWm0Uas4fBaVsO4k53f6Wru531rSviPs2TK9t9Gqx9B9P8hVKbdp2mYFwhL4553FteHundJ+8bH%2Fv9FVUF0JuqX6z%2F5iUqtrJmSgeuLZzXNoOVvKKxBRtw%3D%3D";

            String decryptStr = AESUtil.decryptBase64(encryptStr);

            if (true)return;
            HardcodeFinder finder = new HardcodeFinder();
            finder.operateFind();
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

            /*HardcodeFinder.readStringEnAndTransferAndOutput(
                    "D:\\dyzs_output\\fileTransferEn\\fileEn.txt",
                    "D:\\dyzs_output\\fileTransferEn\\strings.xml",
                    "D:\\dyzs_output\\fileTransferEn\\outputStrings.xml"
            );*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
