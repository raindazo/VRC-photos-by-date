
import exception.MyException;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * PowerShellでフォーム作成・一時ファイルに値保存
 *
 * @author raindazo
 */
public class SettingGUI {
    static final String EMPTY = "";

    public SettingGUI(String[] parameter) {
        final String vrcPictureFilePath = (parameter.length > 0 && !parameter[0].isEmpty()) ? parameter[0] : EMPTY;
        final String createPictureFilePath = (parameter.length > 0 && !parameter[1].isEmpty()) ? parameter[1] : EMPTY;
        try {
            // PowerShellスクリプトをJavaコード内に埋め込む
            String powershellScript =
                    "$ErrorActionPreference = 'Stop'\n" +
                            "# 必要なアセンブリの読み込み\n" +
                            "Add-Type -AssemblyName System.Windows.Forms\n" +
                            "Add-Type -AssemblyName System.Drawing\n" +
                            "\n" +
                            "# フォーム作成\n" +
                            "$form = New-Object System.Windows.Forms.Form\n" +
                            "$form.Text = 'VRC写真日付分けツール'\n" +
                            "$form.Size = New-Object System.Drawing.Size(330, 200)\n" +
                            "\n" +
                            "# 【VRCの写真が格納されている場所】選択ボタン\n" +
                            "$button1 = New-Object System.Windows.Forms.Button\n" +
                            "$button1.Text = 'VRCの写真が格納されている場所を選択'\n" +
                            "$button1.Size = New-Object System.Drawing.Size(120, 30)\n" +
                            "$button1.Location = New-Object System.Drawing.Point(10, 10)\n" +
                            "\n" +
                            "# 【VRCの写真が格納されている場所】表示テキストボックス\n" +
                            "$textBox1 = New-Object System.Windows.Forms.TextBox\n" +
                            "$textBox1.Size = New-Object System.Drawing.Size(160, 20)\n" +
                            "$textBox1.Location = New-Object System.Drawing.Point(140, 10)\n" +
                            "\n" +
                            "# 【撮影日分けされたファイルを保存する場所】選択ボタン\n" +
                            "$button2 = New-Object System.Windows.Forms.Button\n" +
                            "$button2.Text = '撮影日分けされたファイルを保存する場所を選択'\n" +
                            "$button2.Size = New-Object System.Drawing.Size(120, 30)\n" +
                            "$button2.Location = New-Object System.Drawing.Point(10, 50)\n" +
                            "\n" +
                            "# 【撮影日分けされたファイルを保存する場所】表示テキストボックス\n" +
                            "$textBox2 = New-Object System.Windows.Forms.TextBox\n" +
                            "$textBox2.Size = New-Object System.Drawing.Size(160, 20)\n" +
                            "$textBox2.Location = New-Object System.Drawing.Point(140, 50)\n" +
                            "# 実行ボタン\n" +
                            "$buttonRun = New-Object System.Windows.Forms.Button\n" +
                            "$buttonRun.Text = '実行'\n" +
                            "$buttonRun.Size = New-Object System.Drawing.Size(100, 30)\n" +
                            "$buttonRun.Location = New-Object System.Drawing.Point(10, 110)\n" +
                            "\n" +
                            "# 変数定義\n" +
                            "$vrcPictureFilePath = '" + vrcPictureFilePath + "'\n" +
                            "$createPictureFilePath = '" + createPictureFilePath + "'\n" +
                            "\n" +
                            "if ($vrcPictureFilePath -ne '') {\n" +
                            "       $textBox1.Text = $vrcPictureFilePath\n" +
                            "}\n" +
                            "if ($createPictureFilePath -ne '') {\n" +
                            "       $textBox2.Text = $createPictureFilePath\n" +
                            "}\n" +
                            "# 【VRCの写真が格納されている場所】選択イベント\n" +
                            "$button1.Add_Click({\n" +
                            "    $folderDialog = New-Object System.Windows.Forms.FolderBrowserDialog\n" +
                            "    if ($folderDialog.ShowDialog() -eq 'OK') {\n" +
                            "        $textBox1.Text = $folderDialog.SelectedPath  # 選択したディレクトリパスを表示\n" +
                            "    }\n" +
                            "})\n" +
                            "\n" +
                            "# 【撮影日分けされたファイルを保存する場所】ボタンイベント\n" +
                            "$button2.Add_Click({\n" +
                            "    $folderDialog = New-Object System.Windows.Forms.FolderBrowserDialog\n" +
                            "    if ($folderDialog.ShowDialog() -eq 'OK') {\n" +
                            "        $textBox2.Text = $folderDialog.SelectedPath  # 選択したディレクトリパスを表示\n" +
                            "    }\n" +
                            "})\n" +
                            "\n" +
                            "# 実行ボタンクリックイベント\n" +
                            "$buttonRun.Add_Click({\n" +
                            "    $vrcPictureFilePath = $textBox1.Text\n" +
                            "    $createPictureFilePath = $textBox2.Text\n" +
                            "    if ($vrcPictureFilePath -and $createPictureFilePath -ne $null) {\n" +
                            "        # 設定ファイルに保存\n" +
                            "        $settingFolder = [System.IO.Path]::Combine((Get-Location).Path, \"setting\")\n" +
                            "        # settingフォルダが存在しない場合は作成\n" +
                            "         if (-not (Test-Path $settingFolder)) {\n" +
                            "           New-Item -ItemType Directory -Path $settingFolder\n" +
                            "         }\n" +
                            "        $settingFile = [System.IO.Path]::Combine($settingFolder, \"VRCpbd.setting\")\n" +
                            "        $content = \"$vrcPictureFilePath`r`n$createPictureFilePath\"\n" +
                            "        [System.IO.File]::WriteAllText($settingFile, $content)\n" +
                            "        [System.Windows.Forms.MessageBox]::Show(\"設定が完了しました。\n処理を開始します。\")\n" +
                            "        $form.Close()\n" +
                            "    } else {\n" +
                            "        [System.Windows.Forms.MessageBox]::Show('すべての情報を入力してください。')\n" +
                            "    }\n" +
                            "})\n" +
                            "\n" +
                            "# フォームにコントロールを追加\n" +
                            "$form.Controls.Add($button1)\n" +
                            "$form.Controls.Add($textBox1)\n" +
                            "$form.Controls.Add($button2)\n" +
                            "$form.Controls.Add($textBox2)\n" +
                            "$form.Controls.Add($buttonRun)\n" +
                            "\n" +
                            "# フォームを表示\n" +
                            "$form.ShowDialog()" +
                            "\n";

            // PowerShellスクリプトをファイルに保存
            File tempScript = File.createTempFile("tempScript", ".ps1");
            tempScript.deleteOnExit();  // プログラム終了時に自動的に削除されるように設定
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(tempScript), StandardCharsets.UTF_8)) {
                // BOM を書き込む
                writer.write("\uFEFF");
                writer.write(powershellScript);
            }

            // PowerShellスクリプトを実行
            String command = "powershell.exe -ExecutionPolicy Bypass -File \"" + tempScript.getAbsolutePath() + "\"";
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("ファイルの設定が完了しました。");
            } else {
                System.out.println("実行中にエラーが発生しました。");
            }

        } catch (Exception e) {
            new MyException(e);
        }
    }
}