package bluepaledot.sprites;

import javax.swing.ImageIcon;

public class Enemy extends Sprite {

    private Bomb bomb;

    public Enemy(int x, int y) {

        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);

        var EnemyImg = "src/images/jelly1.png";
        var ii = new ImageIcon(EnemyImg);

        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initExplode(x, y);
        }

        private void initExplode(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var explodeImg = "src/images/explode.png";
            var ii = new ImageIcon(explodeImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}
