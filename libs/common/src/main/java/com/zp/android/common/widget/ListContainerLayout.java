package com.zp.android.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaopan on 15/12/1 16:45
 * e-mail: kangqiao610@gmail.com
 * Version: V1.5
 * 类ListView的布局实现简单类.
 * 支持不同布局的itemview, 只需要指定不同的itemView建造者即可.
 * 可以任意删除已加入的itemView, 只要指定唯一的标示对象(规定为绑定的itemBean实例).
 * <p/>
 * 注: 本例适用于 数据量小的列表, 不需要滚动的. 布局相对规律但有一种或多种item样式的布局方式.
 * 支持对布局中加入的ItemView的任意删除(不能禁用itemBind对象和setId功能, 默认开启),
 * 不支持滚动, 需要外加ScrollView. 支持嵌套使用(高级用法).
 * 不支持更新, 此功能将在V2.0支持.
 */
public class ListContainerLayout extends LinearLayout {

    private ItemViewCreator mItemViewCreator;
    private LayoutInflater mInflater;
    private boolean isClearAllBeforeSetDataList = true;

    public ListContainerLayout(Context context) {
        super(context);
        initView();
    }

    public ListContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ListContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        mInflater = LayoutInflater.from(getContext());
    }

    /**
     * 为此布局指定创建ItemView的实现者.
     * @param itemViewCreator 创建ItemView者
     * @param <Bean>
     */
    public <Bean> void setItemViewCreator(ItemViewCreator<Bean> itemViewCreator) {
        if (mItemViewCreator != itemViewCreator) {
            mItemViewCreator = itemViewCreator;
        }
    }

    /**
     * 设置数据源.
     * @param list
     * @param <Bean>
     */
    public <Bean> void setDataList(List<Bean> list) {
        if (isClearAllBeforeSetDataList) removeAllItemViews();
        if (null != list && !list.isEmpty()) {
            for (Bean bean : list) {
                addItemView(bean);
            }
        }
    }

    /**
     * 设置数据源[支持JSONArray类型]
     * @param jarr
     */
    public void setDataList(JSONArray jarr) {
        if (null != jarr && jarr.length() > 0) {
            List<JSONObject> list = new ArrayList<>(jarr.length());
            for (int i = 0; i < jarr.length(); i++) {
                list.add(jarr.optJSONObject(i));
            }
            setDataList(list);
        }
    }

    public <Bean> void appendDataList(ItemViewCreator<Bean> creator, List<Bean> list) {
        setIsClearAllBeforeSetDataList(false);
        setItemViewCreator(creator);
        setDataList(list);
        setIsClearAllBeforeSetDataList(true);
    }

    /**
     * 为此layout添加一个子itemView.
     * @param itemBean 数据源.
     * @param <Bean>   规定并统一Bean类型.
     * @return 链式: 单独使用会最好
     */
    public <Bean> ListContainerLayout addItemView(Bean itemBean) {
        if (null != mItemViewCreator && null != itemBean) {
            View itemView = mItemViewCreator.onCreateItemView(mInflater, this);
            if (null != itemView) {
                mItemViewCreator.setDataForItemView(itemView, itemBean);
                super.addView(itemView);
            }
        }
        return this;
    }

    /**
     * 为此layout添加一个子itemView.
     * 可以更改创建itemView的实现, 此方法可用于<<不同的itemView布局>>中, 只为其指定不同的创建实现者即可.
     * @param itemViewCreator 创建ItemView者
     * @param itemBean        数据源.
     * @param <Bean>          规定并统一Bean类型.
     * @return 链式: 单独使用会最好
     */
    public <Bean> ListContainerLayout addItemView(ItemViewCreator<Bean> itemViewCreator, Bean itemBean) {
        setItemViewCreator(itemViewCreator);
        return addItemView(itemBean);
    }

    /**
     * 获取创建者创建的所有Itemview绑定的数据源.
     * @param creator
     * @param <Bean>
     * @return
     */
    public <Bean> List<Bean> getDataList(ItemViewCreator<Bean> creator) {
        List<View> itemViewList = getItemViewList(creator);
        List<Bean> dataList = null;
        if (null != itemViewList && !itemViewList.isEmpty()) {
            dataList = new ArrayList<Bean>();
            for (View view : itemViewList) {
                Bean bindBean = creator.getBindObject(view);
                if (null != bindBean) {
                    dataList.add(bindBean);
                }
            }
        }
        return dataList;
    }

    /**
     * 如果不指定ItemView创建者, 则使用默认的创建者.
     * @param <Bean>
     * @return
     */
    public <Bean> List<Bean> getDataList() {
        if (null != mItemViewCreator) {
            return getDataList(mItemViewCreator);
        }
        return null;
    }

    /**
     * 获取给定创建者创建的所有ItemView.
     * @param creator
     * @param <Bean>
     * @return
     */
    public <Bean> List<View> getItemViewList(ItemViewCreator<Bean> creator) {
        if (null != creator) {
            int childViewCount = getChildCount();
            if (0 < childViewCount) {
                List<View> creatorItemViewList = new ArrayList<>();
                for (int i = 0; i < childViewCount; i++) {
                    View childView = getChildAt(i);
                    //这里是获取创建者所创建的所有ItemView, 不需要关注ItemView是否绑定了数据源.
                    if (creator.generateItemViewId(childView.getTag()) == childView.getId()) {
                        creatorItemViewList.add(childView);
                    }
                }
                if (!creatorItemViewList.isEmpty()) return creatorItemViewList;
            }
        }
        return null;
    }

    public List<View> getItemViewList() {
        return getItemViewList(mItemViewCreator);
    }

    public <Bean> View getItemView(ItemViewCreator<Bean> creator, Bean itemBean) {
        if (null != creator && creator.isBindItemView(itemBean)) { //如果ItemView绑定了数据源,
            View view = findViewWithTag(itemBean);
            if (null != view && creator.generateItemViewId(itemBean) == view.getId()) {
                return view;
            }
        }
        //否则说明创建者在创建ItemView时没有绑定数据源, 即不能通过数据源来找到布局中的指定的ItemView.
        //    当创建了多个ItemView时, 创建者的generateItemViewId(Bean)方法所产生的Id者是一样的. 固不能找到指定的itemBean.
        return null;
    }

    /**
     * 默认使用当前的创建者去删除这个itemBean绑定的对象.
     * @param itemBean
     * @param <Bean>
     * @return
     */
    public <Bean> boolean removeItemView(Bean itemBean) {
        return removeItemView(mItemViewCreator, itemBean);
    }

    /**
     * 删除创建者creator及itemBean绑定的对象. (仅通过绑定的对象删除).如果没有绑定, 将无法删除之.
     * 如果 创建者为空, 则默认就不依赖创建者去删除这个itemBean, 直接通过findViewWithTag去找到它然后删除之.
     * 注意: 本例通过泛型保证了creator与itemBean的正确绑定. 仅以创建者的泛型<Bean>为主.
     * @param creator
     * @param itemBean
     * @param <Bean>
     * @return
     */
    public <Bean> boolean removeItemView(ItemViewCreator<Bean> creator, Bean itemBean) {
        if (null != creator) {
            View itemView = getItemView(creator, itemBean);
            if (null != itemView) {
                super.removeView(itemView);
                return true;
            }
        }
        return false;
    }

    /**
     * 删除ItemView创建者的所创建的所有ItemView.
     * @param creator
     * @param <Bean>
     * @return
     */
    public <Bean> boolean removeAllItemViews(ItemViewCreator<Bean> creator) {
        List<View> itemViewList = getItemViewList(creator);
        if (null != itemViewList && !itemViewList.isEmpty()) {
            for (int i = itemViewList.size() - 1; i >= 0; i--) {
                removeView(itemViewList.get(i));
            }
            return true;
        }
        return false;
    }

    /**
     * 清空所有数据.
     */
    public void removeAllItemViews() {
        removeAllViews();
    }

    /**
     * 在setDataList设置list数据到layout中时, 是否先清除所有, 然后再添加新的item.
     * @param isClearAllBeforeSetDataList 默认true: 先清除所有views; false: 此为追加views.
     */
    public void setIsClearAllBeforeSetDataList(boolean isClearAllBeforeSetDataList) {
        this.isClearAllBeforeSetDataList = isClearAllBeforeSetDataList;
    }

    /**
     * ItemView的创建者
     * @param <Bean> 规定并统一Bean类型.
     */
    public static abstract class ItemViewCreator<Bean> {
        protected View mItemView;

        protected View getItemView() {
            return mItemView;
        }

        protected void setText(int resId, CharSequence content) {
            View view = findView(resId);
            if (null != view) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(content);
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(content);
                } else if (view instanceof Button) {
                    ((Button) view).setText(content);
                }
            }
        }

        protected void setText(int resId, int content) {
            setText(resId, mItemView.getContext().getResources().getText(content));
        }

        protected void setOnClickListener(int resId, OnClickListener listener) {
            View view = findView(resId);
            if (null != view) {
                view.setOnClickListener(listener);
            }
        }

        protected <T extends View> T findView(int id) {
            return (T) mItemView.findViewById(id);
        }

        /**
         * 用itemBean去标示这个创建的唯一的ItemView.
         * 通过覆写此方法来决定创建者的这个itemView的唯一标示.
         * 如果不想为ItemView绑定数据源, 请直接覆盖其并返回null;
         * 同样的你将失去删除itemview的权力.
         * @param itemBean
         * @return
         */
        protected Object generateItemViewBindObj(Bean itemBean) {
            return itemBean;
        }

        private boolean isBindItemView(Bean itemBean) {
            return null != generateItemViewBindObj(itemBean);
        }

        protected void setBindObj(Object beanObj) {
            mItemView.setTag(beanObj);
        }

        protected abstract View onCreateItemView(LayoutInflater inflater, ViewGroup container);

        protected abstract void setData(Bean itemBean);

        protected void setDataForItemView(View itemView, Bean itemBean) {
            this.mItemView = itemView;
            setBindObj(generateItemViewBindObj(itemBean));
            mItemView.setId(generateItemViewId(itemBean));
            setData(itemBean);
        }

        /**
         * 默认用创建者和数据源bean的hashCode()组合来唯一指定每一个ItemView的id值.
         * 注: 可以覆写, 但不建议.
         * @param bean
         * @return
         */
        protected int generateItemViewId(Object bean) {
            int beanHashCode = (null != bean) ? bean.hashCode() : 0;
            return this.hashCode() + beanHashCode;
        }

        public Bean getBindObject(View itemView) {
            if (null != itemView) {
                Object tagObject = itemView.getTag();
                if (null != tagObject && itemView.getId() == generateItemViewId(tagObject)) {
                    return (Bean) tagObject;
                }
            }
            return null;
        }

        /**
         * 从listContainer中删除itemBean绑定的itemView.
         * @param listContainerLayout
         * @param itemBean
         * @return
         */
        protected boolean removeItemView(ListContainerLayout listContainerLayout, Bean itemBean) {
            if (null != listContainerLayout) {
                return listContainerLayout.removeItemView(this, itemBean);
            }
            return false;
        }
    }
}