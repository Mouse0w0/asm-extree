package com.github.mouse0w0.asm.extree;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.ModuleNode;
import org.objectweb.asm.tree.RecordComponentNode;
import org.objectweb.asm.tree.UnsupportedClassVersionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassNodeEx extends ClassVisitor {
    /**
     * The class version. The minor version is stored in the 16 most significant bits, and the major
     * version in the 16 least significant bits.
     */
    public int version;

    /**
     * The class's access flags (see {@link org.objectweb.asm.Opcodes}). This field also indicates if
     * the class is deprecated {@link Opcodes#ACC_DEPRECATED} or a record {@link Opcodes#ACC_RECORD}.
     */
    public int access;

    /**
     * The internal name of this class (see {@link org.objectweb.asm.Type#getInternalName}).
     */
    public String name;

    /**
     * The signature of this class. May be {@literal null}.
     */
    public String signature;

    /**
     * The internal of name of the super class (see {@link org.objectweb.asm.Type#getInternalName}).
     * For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     * {@link Object} class.
     */
    public String superName;

    /**
     * The internal names of the interfaces directly implemented by this class (see {@link
     * org.objectweb.asm.Type#getInternalName}).
     */
    public List<String> interfaces;

    /**
     * The name of the source file from which this class was compiled. May be {@literal null}.
     */
    public String sourceFile;

    /**
     * The correspondence between source and compiled elements of this class. May be {@literal null}.
     */
    public String sourceDebug;

    /**
     * The module stored in this class. May be {@literal null}.
     */
    public ModuleNode module;

    /**
     * The internal name of the enclosing class of this class. May be {@literal null}.
     */
    public String outerClass;

    /**
     * The name of the method that contains this class, or {@literal null} if this class is not
     * enclosed in a method.
     */
    public String outerMethod;

    /**
     * The descriptor of the method that contains this class, or {@literal null} if this class is not
     * enclosed in a method.
     */
    public String outerMethodDesc;

    public Map<String, AnnotationNodeEx> annotations;

    public Map<String, TypeAnnotationNodeEx> typeAnnotations;

    /**
     * The non standard attributes of this class. May be {@literal null}.
     */
    public List<Attribute> attrs;

    /**
     * The inner classes of this class.
     */
    public List<InnerClassNode> innerClasses;

    /**
     * The internal name of the nest host class of this class. May be {@literal null}.
     */
    public String nestHostClass;

    /**
     * The internal names of the nest members of this class. May be {@literal null}.
     */
    public List<String> nestMembers;

    /**
     * <b>Experimental, use at your own risk. This method will be renamed when it becomes stable, this
     * will break existing code using it</b>. The internal names of the permitted subtypes of this
     * class. May be {@literal null}.
     *
     * @deprecated this API is experimental.
     */
    @Deprecated
    public List<String> permittedSubtypesExperimental;

    /**
     * The record components of this class. May be {@literal null}.
     */
    public List<RecordComponentNode> recordComponents;

    /**
     * The fields of this class.
     */
    public Map<String, FieldNodeEx> fields;

    /**
     * The methods of this class.
     */
    public Map<Method, MethodNodeEx> methods;

    /**
     * Constructs a new {@link org.objectweb.asm.tree.ClassNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #ClassNodeEx(int)} version.
     *
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public ClassNodeEx() {
        this(Opcodes.ASM8);
    }

    /**
     * Constructs a new {@link org.objectweb.asm.tree.ClassNode}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of {@link
     *            Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6}, {@link Opcodes#ASM7} or {@link
     *            Opcodes#ASM8}.
     */
    public ClassNodeEx(final int api) {
        super(api);
        this.interfaces = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
        this.fields = new LinkedHashMap<>();
        this.methods = new LinkedHashMap<>();
    }

    public AnnotationNodeEx getAnnotation(String descriptor) {
        return annotations == null ? null : annotations.get(descriptor);
    }

    public void addAnnotation(AnnotationNodeEx annotation) {
        if (annotations == null) {
            annotations = new LinkedHashMap<>(2);
        }
        annotations.put(annotation.desc, annotation);
    }

    public TypeAnnotationNodeEx getTypeAnnotation(String descriptor) {
        return typeAnnotations == null ? null : typeAnnotations.get(descriptor);
    }

    public void addTypeAnnotation(TypeAnnotationNodeEx typeAnnotation) {
        if (typeAnnotations == null) {
            typeAnnotations = new LinkedHashMap<>(2);
        }
        typeAnnotations.put(typeAnnotation.desc, typeAnnotation);
    }

    public FieldNodeEx getField(String name) {
        return fields.get(name);
    }

    public void addField(FieldNodeEx field) {
        fields.put(field.name, field);
    }

    public MethodNodeEx getMethod(Method method) {
        return methods.get(method);
    }

    public void addMethod(MethodNodeEx method) {
        methods.put(new Method(method.name, method.desc), method);
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the ClassVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.superName = superName;
        this.interfaces = Util.asArrayList(interfaces);
    }

    @Override
    public void visitSource(final String file, final String debug) {
        sourceFile = file;
        sourceDebug = debug;
    }

    @Override
    public ModuleVisitor visitModule(final String name, final int access, final String version) {
        module = new ModuleNode(name, access, version);
        return module;
    }

    @Override
    public void visitNestHost(final String nestHost) {
        this.nestHostClass = nestHost;
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String descriptor) {
        outerClass = owner;
        outerMethod = name;
        outerMethodDesc = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        AnnotationNodeEx annotation = new AnnotationNodeEx(descriptor, visible);
        addAnnotation(annotation);
        return annotation;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        TypeAnnotationNodeEx typeAnnotation = new TypeAnnotationNodeEx(typeRef, typePath, descriptor, visible);
        addTypeAnnotation(typeAnnotation);
        return typeAnnotation;
    }

    @Override
    public void visitAttribute(final Attribute attribute) {
        attrs = Util.add(attrs, attribute);
    }

    @Override
    public void visitNestMember(final String nestMember) {
        nestMembers = Util.add(nestMembers, nestMember);
    }

    /**
     * <b>Experimental, use at your own risk.</b>.
     *
     * @param permittedSubtype the internal name of a permitted subtype.
     * @deprecated this API is experimental.
     */
    @Override
    @Deprecated
    public void visitPermittedSubtypeExperimental(final String permittedSubtype) {
        permittedSubtypesExperimental = Util.add(permittedSubtypesExperimental, permittedSubtype);
    }

    @Override
    public void visitInnerClass(
            final String name, final String outerName, final String innerName, final int access) {
        InnerClassNode innerClass = new InnerClassNode(name, outerName, innerName, access);
        innerClasses.add(innerClass);
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(
            final String name, final String descriptor, final String signature) {
        RecordComponentNode recordComponent = new RecordComponentNode(name, descriptor, signature);
        recordComponents = Util.add(recordComponents, recordComponent);
        return recordComponent;
    }

    @Override
    public FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        FieldNodeEx field = new FieldNodeEx(access, name, descriptor, signature, value);
        fields.put(name, field);
        return field;
    }

    @Override
    public MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        MethodNodeEx method = new MethodNodeEx(access, name, descriptor, signature, exceptions);
        methods.put(new Method(name, descriptor), method);
        return method;
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    // -----------------------------------------------------------------------------------------------
    // Accept method
    // -----------------------------------------------------------------------------------------------

    /**
     * Checks that this class node is compatible with the given ASM API version. This method checks
     * that this node, and all its children recursively, do not contain elements that were introduced
     * in more recent versions of the ASM API than the given version.
     *
     * @param api an ASM API version. Must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5},
     *            {@link Opcodes#ASM6}, {@link Opcodes#ASM7}. or {@link Opcodes#ASM8}.
     */
    public void check(final int api) {
        if (api != Opcodes.ASM9_EXPERIMENTAL && permittedSubtypesExperimental != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM8 && ((access & Opcodes.ACC_RECORD) != 0 || recordComponents != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM7 && (nestHostClass != null || nestMembers != null)) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM6 && module != null) {
            throw new UnsupportedClassVersionException();
        }
        if (api < Opcodes.ASM5) {
            if (typeAnnotations != null && !typeAnnotations.isEmpty()) {
                throw new UnsupportedClassVersionException();
            }
        }
        // Check the annotations.
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.check(api);
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.check(api);
            }
        }
        if (recordComponents != null) {
            for (int i = recordComponents.size() - 1; i >= 0; --i) {
                recordComponents.get(i).check(api);
            }
        }
        for (int i = fields.size() - 1; i >= 0; --i) {
            fields.get(i).check(api);
        }
        for (int i = methods.size() - 1; i >= 0; --i) {
            methods.get(i).check(api);
        }
    }

    /**
     * Makes the given class visitor visit this class.
     *
     * @param classVisitor a class visitor.
     */
    public void accept(final ClassVisitor classVisitor) {
        // Visit the header.
        String[] interfacesArray = new String[this.interfaces.size()];
        this.interfaces.toArray(interfacesArray);
        classVisitor.visit(version, access, name, signature, superName, interfacesArray);
        // Visit the source.
        if (sourceFile != null || sourceDebug != null) {
            classVisitor.visitSource(sourceFile, sourceDebug);
        }
        // Visit the module.
        if (module != null) {
            module.accept(classVisitor);
        }
        // Visit the nest host class.
        if (nestHostClass != null) {
            classVisitor.visitNestHost(nestHostClass);
        }
        // Visit the outer class.
        if (outerClass != null) {
            classVisitor.visitOuterClass(outerClass, outerMethod, outerMethodDesc);
        }
        // Visit the annotations.
        if (annotations != null) {
            for (AnnotationNodeEx annotation : annotations.values()) {
                annotation.accept(classVisitor.visitAnnotation(annotation.desc, annotation.visible));
            }
        }
        if (typeAnnotations != null) {
            for (TypeAnnotationNodeEx typeAnnotation : typeAnnotations.values()) {
                typeAnnotation.accept(
                        classVisitor.visitTypeAnnotation(
                                typeAnnotation.typeRef, typeAnnotation.typePath, typeAnnotation.desc, typeAnnotation.visible));
            }
        }
        // Visit the non standard attributes.
        if (attrs != null) {
            for (int i = 0, n = attrs.size(); i < n; ++i) {
                classVisitor.visitAttribute(attrs.get(i));
            }
        }
        // Visit the nest members.
        if (nestMembers != null) {
            for (int i = 0, n = nestMembers.size(); i < n; ++i) {
                classVisitor.visitNestMember(nestMembers.get(i));
            }
        }
        // Visit the permitted subtypes.
        if (permittedSubtypesExperimental != null) {
            for (int i = 0, n = permittedSubtypesExperimental.size(); i < n; ++i) {
                classVisitor.visitPermittedSubtypeExperimental(permittedSubtypesExperimental.get(i));
            }
        }
        // Visit the inner classes.
        for (int i = 0, n = innerClasses.size(); i < n; ++i) {
            innerClasses.get(i).accept(classVisitor);
        }
        // Visit the record components.
        if (recordComponents != null) {
            for (int i = 0, n = recordComponents.size(); i < n; ++i) {
                recordComponents.get(i).accept(classVisitor);
            }
        }
        // Visit the fields.
        for (int i = 0, n = fields.size(); i < n; ++i) {
            fields.get(i).accept(classVisitor);
        }
        // Visit the methods.
        for (int i = 0, n = methods.size(); i < n; ++i) {
            methods.get(i).accept(classVisitor);
        }
        classVisitor.visitEnd();
    }
}
