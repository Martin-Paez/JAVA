package frontEnd.frames.boardsListFrame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import backEnd.fileManagement.boardsNameReader.BoardsNameReader;

import exceptions.DuplicateName;
import exceptions.WrongDataException;


public class BoardsListFrame extends JDialog {
	List list;
	JTextField name;
	BoardsNameReader reader;

	/**
	 *  Crea una ventana para elejir el mapa y el nombre del héroe.
	 *  
	 * @param listener	-	Listener para informar que el usuario a termindo de ingresar los datos
	 *
	 * @throws FileNotFoundException	-	Si no hay mapas en la carpeta boards.
	 * @throws WrongDataException		-	Si la información de alguno de los mapas en la carpeta boards tiene información incorrecta
	 * @throws DuplicateName			-	Si hay dos mapas en la carpeta boards con el mismo nombre.
	 * @throws IOException
	 */
	public BoardsListFrame(final BoardsListListener listener) throws FileNotFoundException,
		WrongDataException, IOException, DuplicateName {
		
		Stack<String> names = null;
		reader = new BoardsNameReader();
		
		names = reader.refreshBoards();
		list = new List(5);
		while (!names.isEmpty()) {
			list.add(names.pop());
		}

		name = new JTextField("Elija un nombre");

		JButton button = new JButton("ok");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String item = list.getSelectedItem();
				String text = name.getText();
				if (item != null) {
					listener.okBotton(reader.getFileName(item), text);
					BoardsListFrame.this.dispose();
				}
			}
		});

		setBounds(50, 50, 150, 160);
		setResizable(false);
		add(list, BorderLayout.NORTH);
		add(name, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);

		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setVisible(true);

	}

}
