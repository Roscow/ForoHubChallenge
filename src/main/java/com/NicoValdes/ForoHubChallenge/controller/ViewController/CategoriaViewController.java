package com.NicoValdes.ForoHubChallenge.controller.ViewController;
import com.NicoValdes.ForoHubChallenge.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.NicoValdes.ForoHubChallenge.models.Categoria;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/category")
public class CategoriaViewController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String getAllCategorias(Model model) {
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        return "categoria_lista";
    }

    @GetMapping("/{id}")
    public String getCategoriaById(@PathVariable Integer id, Model model) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "categoria_detail"; // Nombre del template para la vista detallada
        } else {
            return "404"; // Nombre del template para la página 404
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditCategoriaForm(@PathVariable Integer id, Model model) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "categoria_form"; // Nombre del template para el formulario de edición
        } else {
            return "404"; // Nombre del template para la página 404
        }
    }


    @GetMapping("/new")
    public String showNewCategoriaForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categoria_form"; // Nombre del template para el formulario de creación
    }
}
