package com.bkt.basdk

import com.bkt.contract.ba.model.CodeDescModel
import com.xxf.arch.XXF
import com.xxf.arch.json.JsonUtils
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @Description: 生成json
   * @Author: XGod  xuanyouwu@163.com  17611639080  https://github.com/NBXXF     https://blog.csdn.net/axuanqq
 * @CreateDate: 2020/12/17 10:34
 */
class CodeDescUtil {


    companion object {
        fun test() {
            val fromAssets: String = getFromAssets("ba_code_test")!!
            val split = fromAssets.split("--".toRegex()).toTypedArray()
            val chinaList: MutableList<CodeDescModel> = mutableListOf();
            val enList: MutableList<CodeDescModel> = mutableListOf();
            split.forEach {
                val split1 = it.split("@".toRegex());
                if (split1.size > 1) {
                    //  XXF.getLogger().d("============>xx22:" + split1.get(0).substring(0, 4))
                    var code: Int = split1.get(0).substring(0, 4).toInt();
                    if (split1.size == 2) {
                        var desc = split1.get(1);
                        chinaList.add(CodeDescModel(-code, desc));

                        enList.add(CodeDescModel(-code, desc));
                    } else if (split1.size > 2) {
                        var desc = split1.get(1);
                        chinaList.add(CodeDescModel(-code, desc));

                        enList.add(CodeDescModel(-code, split1.get(2)));
                    }
                }
            }

            XXF.getFileService().putPrivateFile("test.txt", JsonUtils.toJsonString(chinaList), false).subscribe();
            XXF.getFileService().putPrivateFile("test2.txt", JsonUtils.toJsonString(enList), false).subscribe();
            System.out.println("==========>s:" + chinaList.size);
        }

        fun getFromAssets(fileName: String?): String? {
            try {
                val inputReader = InputStreamReader(XXF.getApplication().resources.assets.open(fileName!!))
                val bufReader = BufferedReader(inputReader)
                var line: String? = ""
                var Result: String? = ""
                while (bufReader.readLine().also { line = it } != null) Result += line
                return Result
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}