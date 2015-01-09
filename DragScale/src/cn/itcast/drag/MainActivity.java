package cn.itcast.drag;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView imageView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setOnTouchListener(new TouchListener());
    }
    
    private final class TouchListener implements OnTouchListener{
    	private PointF startPoint = new PointF();
    	private Matrix matrix = new Matrix();
    	private Matrix currentMatrix = new Matrix();
    	private int mode = 0;
    	private static final int DRAG = 1;
    	private static final int ZOOM = 2;
    	private float startDis;//��ʼ����
    	private PointF midPoint;//�м��
    	
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN://��ָѹ����Ļ
				mode = DRAG;
				currentMatrix.set(imageView.getImageMatrix());//��¼ImageView��ǰ���ƶ�λ��
				startPoint.set(event.getX(), event.getY());
				break;

			case MotionEvent.ACTION_MOVE://��ָ����Ļ�ƶ����� �¼��᲻�ϵش���
				if(mode == DRAG){
					float dx = event.getX() - startPoint.x;//�õ���x����ƶ�����
					float dy = event.getY() - startPoint.y;//�õ���y����ƶ�����
					matrix.set(currentMatrix);//��û�н����ƶ�֮ǰ��λ�û����Ͻ����ƶ�
					matrix.postTranslate(dx, dy);
				}else if(mode == ZOOM){//����
					float endDis = distance(event);//��������
					if(endDis > 10f){
						float scale = endDis / startDis;//�õ����ű���
						matrix.set(currentMatrix);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}				
				break;
				
			case MotionEvent.ACTION_UP://��ָ�뿪��
			case MotionEvent.ACTION_POINTER_UP://����ָ�뿪��Ļ,����Ļ���д��㣨��ָ��
				mode = 0;
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN://����Ļ�ϻ��д��㣨��ָ��������һ����ָѹ����Ļ
				mode = ZOOM;
				startDis = distance(event);
				if(startDis > 10f){
					midPoint = mid(event);
					currentMatrix.set(imageView.getImageMatrix());//��¼ImageView��ǰ�����ű���
				}
				break;
			}
			imageView.setImageMatrix(matrix);
			return true;
		}
    	
    }
    /**
     * ��������֮��ľ���
     * @param event
     * @return
     */
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx*dx + dy*dy);
	}
	/**
	 * ��������֮����м��
	 * @param event
	 * @return
	 */
	public static PointF mid(MotionEvent event){
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}
}