var gulp = require('gulp');
var del = require('del');
var mustache = require('gulp-mustache');
var rename = require('gulp-rename');
var debug = require('gulp-debug');
var gulpdata = require('gulp-data');
var sass = require('gulp-sass');


gulp.task('clean', function () {
    return del(['build']);
});

gulp.task('compile', ['clean'], function () {
    gulp.src("./static-site/templates/index.mustache")
        .pipe(mustache('./static-site/config/index.json'))
        .pipe(rename(function (path) {
            path.extname = ".html"
        }))
        .pipe(gulp.dest("./target/classes/site"));

    gulp.src("./static-site/portfolio/*.json")
        .pipe(gulpdata(function (file) {
            var data = JSON.parse(String(file.contents));
            data["header"] = {"title": data.blog.title};
            var filename = file.path.slice(file.base.length, (file.path.length - 5));
            return gulp.src('./static-site/templates/portfolio.mustache')
                .pipe(mustache(data))
                .pipe(rename(filename + ".html"))
                .pipe(gulp.dest("./target/classes/site"));
        }));
    gulp.src('./static-site/scss/**/*.scss')
        .pipe(debug())
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest('./target/classes/site/css'));
    gulp.src('./static-site/css/*')
        .pipe(gulp.dest('./target/classes/site/css'));

});


gulp.task('default', []);


