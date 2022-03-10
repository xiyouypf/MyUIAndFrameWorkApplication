package com.tencent.mini.myredpacket;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class RedEnvelopeService extends AccessibilityService {
    private String[] filter = new String[]{"恭喜发财，大吉大利", "开", "已领取", "已存入零钱，可直接提现", "已被领完"};

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo rootNodeInfo = event.getSource();
        if (rootNodeInfo == null) {
            return;
        }

//        if (isNeedToGrab(rootNodeInfo)) {
//            if (!first) {
//                startFirst(rootNodeInfo);
//            } else if (!second) {
//                startSecond(rootNodeInfo);
//            }
//        }
        if (isNeedFirst(rootNodeInfo)) {
            startFirst(rootNodeInfo);
        } else if (isNeedSecond(rootNodeInfo)) {
            startSecond(rootNodeInfo);
        }
    }

    private boolean isNeedFirst(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list1 = rootNodeInfo.findAccessibilityNodeInfosByText("恭喜发财，大吉大利");
        List<AccessibilityNodeInfo> list2 = rootNodeInfo.findAccessibilityNodeInfosByText("已领取");
        List<AccessibilityNodeInfo> list4 = rootNodeInfo.findAccessibilityNodeInfosByText("已被领完");
        if (list2.size() + list4.size() >= list1.size()) {
            return false;
        }
        List<AccessibilityNodeInfo> kai = rootNodeInfo.findAccessibilityNodeInfosByText("开");
        if (kai != null && !kai.isEmpty()) {
            return false;
        }
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText("已存入零钱，可直接提现");
        if (list != null && !list.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean isNeedSecond(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText("开");
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isNeedThird(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText("已存入零钱，可直接提现");
        if (list != null && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean isNeedToGrab(AccessibilityNodeInfo rootNodeInfo) {
        if (rootNodeInfo == null) {
            return false;
        }
//        startClick(rootNodeInfo);
        List<AccessibilityNodeInfo> infos = rootNodeInfo.findAccessibilityNodeInfosByText(filter[3]);
        if (infos != null && !infos.isEmpty()) {
            return false;
        }
        List<AccessibilityNodeInfo> list1 = rootNodeInfo.findAccessibilityNodeInfosByText(filter[0]);
        List<AccessibilityNodeInfo> list2 = rootNodeInfo.findAccessibilityNodeInfosByText(filter[2]);
        List<AccessibilityNodeInfo> list4 = rootNodeInfo.findAccessibilityNodeInfosByText(filter[4]);
        if (list2.size() + list4.size() >= list1.size()) {
            return false;
        }
        return true;
    }

    private void startEnvelope(AccessibilityNodeInfo rootNodeInfo) {
        for (int i = 0; i < rootNodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo child = rootNodeInfo.getChild(i);
            boolean isClick = child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (!isClick) {
                startEnvelope(child);
            }
        }
    }

    private boolean first;
    private boolean second;

    private void startFirst(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText(filter[0]);
        if (list == null || list.isEmpty()) {
            return;
        }
        AccessibilityNodeInfo nodeInfo = list.get(list.size() - 1);
//        for (AccessibilityNodeInfo nodeInfo : list) {
        if (nodeInfo != null) {
            if ("已拆开".equals(nodeInfo.getText()) || "已领取".equals(nodeInfo.getText())) {
                return;
            }
            boolean isClick = nodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (!isClick && !first) {
//                    reClick(rootNodeInfo);
            } else if (isClick) {
                first = true;
                second = false;
            }
        }
//        }
    }

    private void startSecond(AccessibilityNodeInfo rootNodeInfo) {
//        first = false;
//        second = true;
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText(filter[1]);
        if (list == null || list.isEmpty()) {
            return;
        }
//        AccessibilityNodeInfo nodeInfo = list.get(list.size() - 1);
        for (AccessibilityNodeInfo nodeInfo : list) {
            if (nodeInfo != null) {
                if ("已拆开".equals(nodeInfo.getText()) || "已领取".equals(nodeInfo.getText())) {
                    return;
                }
                boolean isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
/*                if (!isClick && !second) {
//                    reClick(rootNodeInfo);
                } else*/
                if (isClick) {
                    first = false;
                    second = true;
                    return;
                }
            }
        }
    }

    private void startClick(AccessibilityNodeInfo rootNodeInfo) {
        List<AccessibilityNodeInfo> list = findByText(rootNodeInfo, filter[0]);
        if (list == null || list.isEmpty()) {
            return;
        }
//        AccessibilityNodeInfo nodeInfo = list.get(list.size() - 1);
        for (AccessibilityNodeInfo nodeInfo : list) {
            if (nodeInfo != null) {
                if ("已拆开".equals(nodeInfo.getText()) || "已领取".equals(nodeInfo.getText())) {
                    return;
                }
                boolean isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (!isClick && !first) {
//                    reClick(rootNodeInfo);
                } else if (isClick) {
                    first = true;
                }
            }
        }
    }

    private void reClick(AccessibilityNodeInfo rootNodeInfo) {
        int childCount = rootNodeInfo.getChildCount();
        if (childCount == 0) {
            rootNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo info = rootNodeInfo.getChild(i);
            if (info == null) {
                continue;
            }
            if (info.getChildCount() > 0) {
                reClick(info);
            } else {
                if (containsEnvelope(info.getText().toString())) {
                    boolean isClick = rootNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    private boolean containsEnvelope(String s) {
        if (TextUtils.isEmpty(s)) {
            return false;
        }
        for (String temp : filter) {
            if (s.equals(temp)) {
                return true;
            }
        }
        return false;
    }

    private List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo rootNodeInfo, String s) {
        List<AccessibilityNodeInfo> list = rootNodeInfo.findAccessibilityNodeInfosByText(s);
        return list;
    }

    @Override
    public void onInterrupt() {

    }
}
