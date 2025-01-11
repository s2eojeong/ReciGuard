import React from 'react';
import './Morerec.css';

const Morerec = () => {
    const categories = [
        { name: '한식', image: 'placeholder.jpg' },
        { name: '중식', image: 'placeholder.jpg' },
        { name: '일식', image: 'placeholder.jpg' },
        { name: '양식', image: 'placeholder.jpg' },
        { name: '아시안', image: 'placeholder.jpg' },
    ];

    return (
        <div className="more-recipes">
            <h2 className="more-recipes-title">더 많은 레시피</h2>
            <div className="recipe-category-container">
                {categories.map((category, index) => (
                    <div className="recipe-category" key={index}>
                        <img
                            src={category.image}
                            alt={category.name}
                            className="recipe-image"
                        />
                        <p className="recipe-name">{category.name}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Morerec;
