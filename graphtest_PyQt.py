import sys
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib
from tkinter import Tk, simpledialog, colorchooser
from tkinter.filedialog import askopenfilenames
from PyQt5.QtWidgets import QApplication, QDialog, QVBoxLayout, QLabel, QLineEdit, QPushButton

matplotlib.use('Qt5Agg')  # Qt を使う場合は明示的に設定

class CustomDialog(QDialog):
    def __init__(self, idx):
        super().__init__()#初期化
        self.idx = idx
        self.initUI()

    def initUI(self):
        self.setWindowTitle("グラフ移動")#メッセージダイアログのタイトル
        self.setGeometry(300, 150, 600, 400)

        layout = QVBoxLayout()#メッセージダイアログ
        self.label = QLabel(f"{self.idx+1}番目のグラフをどれだけ動かしますか？", self)
        layout.addWidget(self.label)

        self.line_edit = QLineEdit(self)
        layout.addWidget(self.line_edit)

        self.button = QPushButton('OK', self)#OKボタン
        self.button.clicked.connect(self.accept)
        layout.addWidget(self.button)

        self.setLayout(layout)

    def get_increment(self):
        try:
            return float(self.line_edit.text())
        except ValueError:
            return 0.0

def choose_color():#色を選択するダイアログ
    color_code = colorchooser.askcolor(title="色を選択してください")
    return color_code[1] if color_code else None

if __name__ == '__main__':#main
    Tk().withdraw()#ファイル選択
    files = askopenfilenames(title="Select .asc files", filetypes=[("ASC files", "*.asc"), ("All files", "*.*")])

    if not files:
        print("No files selected. Exiting...")
        sys.exit()

    app = QApplication(sys.argv)  # `QApplication` を 1 回だけ作成

    plt.figure(figsize=(4.52, 7))
    for idx, file in enumerate(files):
        try:
            record = pd.read_csv(file, header=None, names=["X", "Y"])#.ascは.csvと同じように読み込める

            dialog = CustomDialog(idx)
            increment = 0.0
            if dialog.exec_() == QDialog.Accepted:
                increment = dialog.get_increment()

            record["Y"] += increment  # 直接加算できる

            #グラフ一つ分
            plt.scatter(record["X"],
                        record["Y"],
                        alpha=0.7,
                        color='none',
                        label=file.split("/")[-1]
                        )#scatter == 散布図
            
            color = choose_color()
            plt.plot(record["X"],
                     record["Y"],
                     color=color if color else 'black'
                     )#線でつなぐ

        except Exception as e:
            print(f"Error processing file {file}: {e}")

    #グラフのレイアウト
    plt.xlabel("Wavenumber [cm$^{-1}$]", fontsize=20)
    plt.ylabel("Transmittance", fontsize=20)
    plt.tick_params(labelleft=False, labelright=False, labeltop=False)
    plt.tick_params(left=False, right=False, top=False)
    plt.yticks(color="None")

    ax = plt.gca()
    #枠線の太さ
    for spine in ax.spines.values():
        spine.set_linewidth(2)

    plt.xlim(400, 4000)
    plt.xticks(np.arange(400, 4001, step=600), fontsize=14)
    plt.gca().invert_xaxis()
    plt.gca().tick_params(width=2)

    plt.show()
