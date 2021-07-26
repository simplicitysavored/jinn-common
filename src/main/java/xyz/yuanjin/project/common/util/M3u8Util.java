package xyz.yuanjin.project.common.util;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author yuanjin
 * @date 2021/7/25 5:35 下午
 */
public class M3u8Util {

    static enum STATUS {
        READY,
        DOWNLOADING,
        DOWNLOADED
    }


    public static String getAseName(File getM3u8IndexFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(getM3u8IndexFile));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("#EXT-X-KEY")) {
                break;
            }
        }
        if (null != line) {
            String[] split = line.split(",");
            String URIStr = split[1];
            return URIStr.split("=")[1].replace("\"", "");
        }
        return null;
    }

    /**
     * 缓存视频
     *
     * @param url       m3u8链接
     * @param targetDir 存储目标文件夹路径
     * @param videoCode 视频番号
     * @param async     是否异步执行
     */
    public static void download(
            String url,
            String targetDir,
            String videoCode,
            String videoDesc,
            boolean async
    ) throws NoSuchAlgorithmException, IOException, KeyManagementException, InterruptedException {
        String baseUrl = url.substring(0, url.lastIndexOf("/"));
        String tsBaseDir = targetDir + File.separator + videoCode;
        FileUtil.createFolderIfNotExists(tsBaseDir);

        String m3u8IndexPath = targetDir + File.separator + videoCode + (StringUtil.isNotBlank(videoDesc) ? " (" + videoDesc + ")" : "") + ".index";
        File m3u8IndexFile = new File(m3u8IndexPath);
        FileUtil.delete(m3u8IndexFile);

        HttpUtil.download(url, m3u8IndexPath);

        formatM3u8Text(m3u8IndexFile, videoCode);
        downloadM3u8AseFile(m3u8IndexFile, baseUrl, tsBaseDir);

        String m3u8Text = FileUtil.read(new File(m3u8IndexPath));

        List<String> tsNameList = getTsNameList(m3u8Text);

        int threadNum = 10;

        ConcurrentHashMap<String, STATUS> tsMap = new ConcurrentHashMap<>();
        tsNameList.forEach(tsName -> tsMap.put(tsName, STATUS.READY));

        boolean isFinishDownload = isFinishDownload(new File(tsBaseDir), tsMap);
        if (isFinishDownload) {
            System.out.println("已经全部下载过了，无需重复下载");
            return;
        }

        long startMills = System.currentTimeMillis();

        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
//                System.out.println("线程启动：" + Thread.currentThread().getName());
                String tsName = null;
                while ((tsName = getTs(tsNameList, tsMap)) != null) {
                    String tsUrl = baseUrl + "/" + tsName;
                    String tsPath = tsBaseDir + "/" + tsName;
                    try {
                        Thread.sleep(new Random().nextInt(2000));
                        if (FileUtil.notExists(tsPath)) {
                            HttpUtil.download(tsUrl, tsPath);
                        }
                        setTsDownloaded(tsMap, tsName);
//                        checkFinishProgress(tsMap, startMills);
                    } catch (Exception e) {
                        retryM3u8Download(tsMap, tsName, tsUrl, tsPath, startMills);
                    }

                }
//                System.out.println("线程结束：" + Thread.currentThread().getName());
            }).start();
        }

        if (!async) {
            while (true) {
                Thread.sleep(2000);
                if (checkFinishProgress(tsMap, startMills)) {
                    return;
                }
            }
        }
    }

    private static boolean isFinishDownload(File tsBaseDirFile, ConcurrentHashMap<String, STATUS> tsMap) {
        File[] files = tsBaseDirFile.listFiles();
        if (null != files) {
            for (File file : files) {
                tsMap.put(file.getName(), STATUS.DOWNLOADED);
            }
            return files.length >= tsMap.size();
        }
        return false;
    }

    private static void downloadM3u8AseFile(File m3u8IndexFile, String baseUrl, String tsBaseDir) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        String aseName = getAseName(m3u8IndexFile);
        if (null != aseName) {
            String asePath = tsBaseDir + File.separator + new File(aseName).getName();
            String aseUrl = baseUrl + "/" + new File(aseName).getName();
            try {
                FileUtil.delete(asePath);
                HttpUtil.download(aseUrl, asePath);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void retryM3u8Download(ConcurrentHashMap<String, STATUS> tsMap, String tsName, String tsUrl, String tsPath, long startMills) {
        retryM3u8DownloadRecursion(1, tsMap, tsName, tsUrl, tsPath, startMills);
    }

    private static void retryM3u8DownloadRecursion(int times, ConcurrentHashMap<String, STATUS> tsMap, String tsName, String tsUrl, String tsPath, long startMills) {
        try {
            System.err.println("下载失败，第" + times + "次重新尝试 ｜ " + tsName);
            Thread.sleep(2000);
            HttpUtil.download(tsUrl, tsPath);
            setTsDownloaded(tsMap, tsName);
//            checkFinishProgress(tsMap, startMills);
        } catch (Exception e) {
            if (times > 5) {
                System.err.println(e.getMessage());
                System.err.println("下载失败，不再尝试 ｜ " + tsName);
            } else {
                times++;
                retryM3u8DownloadRecursion(times, tsMap, tsName, tsUrl, tsPath, startMills);
            }
        }
    }


    public static void formatM3u8Text(File m3u8File, String videoCode) throws IOException {
        List<String> lineList = new ArrayList<>();

        String aseName = getAseName(m3u8File);

        try (BufferedReader br = new BufferedReader(new FileReader(m3u8File))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(m3u8File))) {
            for (String lineTmp : lineList) {
                if (lineTmp.startsWith("#")) {
                    if (null != aseName && lineTmp.contains(aseName)) {
                        bw.write(lineTmp.replace(aseName, videoCode + File.separator + aseName));
                    } else {
                        bw.write(lineTmp);
                    }
                } else {
                    if (!"".equals(lineTmp.trim())) {
                        bw.write(videoCode + File.separator + new File(lineTmp).getName());
                    }
                }
                bw.newLine();
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static List<String> getTsNameList(String m3u8Text) {
        List<String> retList = new ArrayList<>();
        String[] lines = m3u8Text.split("\\s+");
        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }
            if (!"".equals(line.trim())) {
                retList.add(new File(line).getName());
            }
        }
        return retList;
    }


    private static synchronized String getTs(List<String> tsNameList, ConcurrentHashMap<String, STATUS> tsMap) {
        for (String tsName : tsNameList) {
            if (STATUS.READY.equals(tsMap.get(tsName))) {
                tsMap.put(tsName, STATUS.DOWNLOADING);
                return tsName;
            }
        }
        return null;
    }


    private static void setTsDownloaded(ConcurrentHashMap<String, STATUS> tsMap, String tsName) {
        tsMap.put(tsName, STATUS.DOWNLOADED);
    }

    private static boolean checkFinishProgress(ConcurrentHashMap<String, STATUS> tsMap, long startMills) {
        List<STATUS> downloadedList = tsMap.values().stream().filter(item -> item.equals(STATUS.DOWNLOADED)).collect(Collectors.toList());
        System.out.println("当前进度：" + downloadedList.size() + "/" + tsMap.size());
        if (downloadedList.size() == tsMap.size()) {
            System.out.println("已完成， 耗时：" + Helper.formatMills((System.currentTimeMillis() - startMills)));
            return true;
        }
        return false;
    }
}
