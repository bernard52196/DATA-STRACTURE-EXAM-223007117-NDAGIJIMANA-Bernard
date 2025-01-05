class TreeNode:
    def __init__(self, name):
        self.name = name
        self.children = []

    def add_child(self, child_node):
        self.children.append(child_node)

    def display(self, level=0):
        print("  " * level + self.name)
        for child in self.children:
            child.display(level + 1)


class ConstructionSystemTree:
    def __init__(self):
        self.root = TreeNode("Construction Materials")

    def add_material(self, category, subcategory, material):
        category_node = self._find_or_create(self.root, category)
        subcategory_node = self._find_or_create(category_node, subcategory)
        self._find_or_create(subcategory_node, material)

    def _find_or_create(self, parent, name):
        for child in parent.children:
            if child.name == name:
                return child
        new_node = TreeNode(name)
        parent.add_child(new_node)
        return new_node

    def display(self):
        self.root.display()



system = ConstructionSystemTree()

system.add_material("Building Materials", "Cement", "Portland Cement")
system.add_material("Building Materials", "Steel", "Rebar Steel")
system.add_material("Finishing Materials", "Paint", "Emulsion Paint")
system.add_material("Finishing Materials", "Tiles", "Ceramic Tiles")

system.add_material("Building Materials", "Cement", "White Cement")
system.add_material("Finishing Materials", "Tiles", "Porcelain Tiles")

system.display()

