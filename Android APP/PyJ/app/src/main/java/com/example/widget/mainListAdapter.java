package com.example.widget;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.datas.List_datas;
import com.example.pyj.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by song on 2017-4-26.
 */

public class mainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<List_datas> list;
    private OnItemClickListener onItemClickListener;
    private mainListAdapter.mainListHolder selectedView;

    /**
     * 重写viewHolder(开始)
     **/
    class mainListHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title;
        TextView genres;
        TextView rating;

        public mainListHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.main_list_pic);
            title = (TextView) itemView.findViewById(R.id.main_list_title);
            genres = (TextView) itemView.findViewById(R.id.main_list_genres);
            rating = (TextView) itemView.findViewById(R.id.main_list_rating);
        }
    }

    /**
     * 重写viewHolder(结束)
     **/

    /**
     * 底部加载效果
     */
    class footHolder extends RecyclerView.ViewHolder {
        View progressBar;
        View loading;

        public footHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.loadingbar);
            loading = view.findViewById(R.id.loading);
        }
    }

    public mainListAdapter(Context context, ArrayList list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param viewGroup ViewHolder的容器
     * @param viewType  一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0)
            return new mainListHolder(layoutInflater.inflate(R.layout.main_list, viewGroup, false));
        else if (viewType == 1)
            return new footHolder(layoutInflater.inflate(R.layout.foot_list, viewGroup, false));
        return null;
    }

    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param position   数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof mainListHolder) {
            final List_datas data = list.get(position);
            selectedView = (mainListHolder) viewHolder;
            ((mainListHolder) viewHolder).title.setText(data.getTitle());
            //获取类型
            ((mainListHolder) viewHolder).genres.setText(data.getGenres());
            //获取评分
            ((mainListHolder) viewHolder).rating.setText(data.getRating());
            //获取图片
            ((mainListHolder) viewHolder).pic.setImageBitmap(data.getPic());
            if (onItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(selectedView.itemView, Integer.parseInt(data.get_id()));
                    }
                });
            }
        } else {
            ((footHolder) viewHolder).progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;
        } else {
            return 0;

        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
        //return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 刷新数据
     *
     * @param datas
     */
    public void RefreshItem(ArrayList<List_datas> datas) {
        if (list == null) {
            list = new ArrayList<>();
            list.addAll(0, datas);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void AddItem(ArrayList<List_datas> datas) {
        list.addAll(list.size(), datas);
        notifyDataSetChanged();
    }
}
/**
 * 重写Adapter（结束）
 **/
